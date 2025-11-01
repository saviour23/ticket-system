package com.au.sportsbet.ticket.system.api.service;

import com.au.sportsbet.ticket.system.api.dto.TicketSummary;
import com.au.sportsbet.ticket.system.api.dto.TransactionRequest;
import com.au.sportsbet.ticket.system.api.dto.TransactionResponse;
import com.au.sportsbet.ticket.system.api.exception.TransactionException;
import com.au.sportsbet.ticket.system.api.mapper.ToCustomerMapper;
import com.au.sportsbet.ticket.system.api.model.Customer;
import com.au.sportsbet.ticket.system.api.model.TicketType;
import com.au.sportsbet.ticket.system.api.service.pricing.TicketPricingStrategy;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

/**
 * Service class to handle the business logic to calculate the cost of ticket bookings.
 */
@Service
@Slf4j
public class TransactionService {
    private static final Logger log = LoggerFactory.getLogger(TransactionService.class);
    @Autowired
    ToCustomerMapper customerMapper;

    private final Map<TicketType, TicketPricingStrategy> pricingStrategies;


    @Autowired
    public TransactionService(List<TicketPricingStrategy> strategies) {
        pricingStrategies = new HashMap<>();
        // populating strategies map
        strategies.forEach(
                strategy -> {
                    pricingStrategies.put(strategy.getTicketType(), strategy);
                });
    }

    public TransactionResponse processTransaction(TransactionRequest request) throws TransactionException {

        try {
            Map<TicketType, List<Customer>> groupedCustomer = new HashMap<>();

            // grouping the customers based on Ticket Type
            request.customers().forEach(customerRequest -> {
                Customer customer = customerMapper.mapToCustomer(customerRequest);
                TicketType type = getCustomerTicketType(customer.age());
                groupedCustomer.computeIfAbsent(type, k -> new ArrayList<>()).add(customer);

            });

            List<TicketSummary> summaries = new ArrayList<>();
            BigDecimal totalCost = BigDecimal.ZERO;

            // calculating the prices based on each ticket type group.
            for (TicketType type : groupedCustomer.keySet()) {
                List<Customer> customers = groupedCustomer.get(type);

                BigDecimal subtotal = customers.stream()
                        .map(customer ->
                                pricingStrategies.get(type).calculatePrice(customer)
                        )
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

                if (type == TicketType.CHILDREN && customers.size() >= 3) {
                    subtotal = subtotal.multiply(BigDecimal.valueOf(0.75)); // 25% discount
                }

                subtotal = subtotal.setScale(2, RoundingMode.HALF_UP);
                summaries.add(new TicketSummary(type.getName(), customers.size(), subtotal));

                totalCost = totalCost.add(subtotal);
            }

            summaries.sort(Comparator.comparing(TicketSummary::ticketType));
            return new TransactionResponse(request.transactionId(), summaries, totalCost);
        } catch (Exception e) {
            String errorMessage = "Error in Transaction Service, error= " + e.getMessage();
            log.error(errorMessage);
            throw new TransactionException(errorMessage, e);

        }

    }

    private TicketType getCustomerTicketType(int age) {
        if (age < 11) return TicketType.CHILDREN;
        if (age < 18) return TicketType.TEEN;
        if (age < 65) return TicketType.ADULT;
        return TicketType.SENIOR;
    }

}

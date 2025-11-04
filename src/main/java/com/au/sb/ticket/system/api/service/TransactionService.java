package com.au.sb.ticket.system.api.service;

import com.au.sb.ticket.system.api.dto.TicketSummary;
import com.au.sb.ticket.system.api.dto.TransactionRequest;
import com.au.sb.ticket.system.api.dto.TransactionResponse;
import com.au.sb.ticket.system.api.exception.TransactionException;
import com.au.sb.ticket.system.api.mapper.ToCustomerMapper;
import com.au.sb.ticket.system.api.model.Customer;
import com.au.sb.ticket.system.api.model.TicketType;
import com.au.sb.ticket.system.api.service.pricing.TicketPricingStrategy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service class to handle the business logic to calculate the cost of ticket bookings.
 */
@Service
@Slf4j
public class TransactionService {

    private ToCustomerMapper customerMapper;

    private final Map<TicketType, TicketPricingStrategy> pricingStrategies;


    @Autowired
    public TransactionService(List<TicketPricingStrategy> strategies, ToCustomerMapper customerMapper) {
        pricingStrategies = new HashMap<>();
        // populating strategies map
        strategies.forEach(
                strategy -> {
                    pricingStrategies.put(strategy.getTicketType(), strategy);
                });

        this.customerMapper = customerMapper;
    }

    /**
     * Method to process the input transaction request and calculate the prices.
     *
     * @param request input request {@link TransactionRequest}
     * @return Response. {@link TransactionResponse}
     * @throws exception {@link TransactionException}
     */
    public TransactionResponse calculateTotalCostForTransaction(TransactionRequest request) throws TransactionException {

        try {

            log.info("Processing Transaction for transactionId = {}", request.transactionId());
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
                log.debug("Sub total for the transactionId= {}, ticketType={}, is= {}", request.transactionId(), type.getName(), subtotal);

                totalCost = totalCost.add(subtotal);
            }

            summaries.sort(Comparator.comparing(TicketSummary::ticketType));
            log.info("Total price calculation complete for transactionId= {} is= {}", request.transactionId(), totalCost);
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

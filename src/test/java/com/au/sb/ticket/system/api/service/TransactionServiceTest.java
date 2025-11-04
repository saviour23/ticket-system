package com.au.sb.ticket.system.api.service;

import com.au.sb.ticket.system.api.dto.CustomerRequest;
import com.au.sb.ticket.system.api.dto.TicketSummary;
import com.au.sb.ticket.system.api.dto.TransactionRequest;
import com.au.sb.ticket.system.api.dto.TransactionResponse;
import com.au.sb.ticket.system.api.exception.TransactionException;
import com.au.sb.ticket.system.api.mapper.ToCustomerMapper;
import com.au.sb.ticket.system.api.model.Customer;
import com.au.sb.ticket.system.api.model.TicketType;
import com.au.sb.ticket.system.api.service.pricing.TicketPricingStrategy;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    private TransactionService transactionService;

    @Mock
    private TicketPricingStrategy adultStrategy;
    @Mock
    private TicketPricingStrategy seniorStrategy;
    @Mock
    private TicketPricingStrategy teenStrategy;
    @Mock
    private TicketPricingStrategy childrenStrategy;

    @Mock
    private ToCustomerMapper mapper;

    @Test
    void testCalculateTotalCostForTransaction_singleAdult() {


        Mockito.when(adultStrategy.calculatePrice(any())).thenReturn(new BigDecimal("25.00"));
        Mockito.when(adultStrategy.getTicketType()).thenReturn(TicketType.ADULT);


        // Inject mocked strategies into service
        transactionService = new TransactionService(
                List.of(adultStrategy), mapper
        );


        CustomerRequest cust = new CustomerRequest("John wick", 30);
        TransactionRequest request = new TransactionRequest(1L, List.of(cust));

        Customer customer = new Customer("John wick", 30);
        Mockito.when(mapper.mapToCustomer(Mockito.any())).thenReturn(customer);


        TransactionResponse response = transactionService.calculateTotalCostForTransaction(request);

        assertEquals(1L, response.transactionId());
        assertEquals(1, response.tickets().size());
        TicketSummary ticket = response.tickets().get(0);
        assertEquals("Adult", ticket.ticketType());
        assertEquals(1, ticket.quantity());
        assertEquals(new BigDecimal("25.00"), ticket.totalCost());
        assertEquals(new BigDecimal("25.00"), response.totalCost());
    }

    @Test
    void testCalculateTotalCostForTransaction_childrenDiscount() {


        Mockito.when(childrenStrategy.calculatePrice(any())).thenReturn(new BigDecimal("5.00"));
        Mockito.when(childrenStrategy.getTicketType()).thenReturn(TicketType.CHILDREN);


        // Inject mocked strategies into service
        transactionService = new TransactionService(
                List.of(childrenStrategy), mapper
        );


        CustomerRequest c1 = new CustomerRequest("Kid1", 5);
        CustomerRequest c2 = new CustomerRequest("Kid2", 6);
        CustomerRequest c3 = new CustomerRequest("Kid3", 8);

        Customer customer1 = new Customer("Kid1", 5);
        Customer customer2 = new Customer("Kid2", 6);
        Customer customer3 = new Customer("Kid3", 8);

        Mockito.when(mapper.mapToCustomer(Mockito.any())).thenReturn(customer1).thenReturn(customer2).thenReturn(customer3);


        TransactionRequest request = new TransactionRequest(2L, List.of(c1, c2, c3));

        TransactionResponse response = transactionService.calculateTotalCostForTransaction(request);

        assertEquals(2L, request.transactionId());
        assertEquals(1, response.tickets().size());
        TicketSummary ticket = response.tickets().get(0);
        assertEquals("Children", ticket.ticketType());
        assertEquals(3, ticket.quantity());

        assertEquals(new BigDecimal("11.25"), ticket.totalCost());
        assertEquals(new BigDecimal("11.25"), response.totalCost());
    }

    @Test
    void testCalculateTotalCostForTransaction_SingleAdultWithChildrenDiscount() {


        Mockito.when(adultStrategy.calculatePrice(any())).thenReturn(new BigDecimal("25.00"));
        Mockito.when(adultStrategy.getTicketType()).thenReturn(TicketType.ADULT);


        Mockito.when(childrenStrategy.calculatePrice(any())).thenReturn(new BigDecimal("5.00"));
        Mockito.when(childrenStrategy.getTicketType()).thenReturn(TicketType.CHILDREN);


        // Inject mocked strategies into service
        transactionService = new TransactionService(
                List.of(adultStrategy, childrenStrategy), mapper
        );


        CustomerRequest a1 = new CustomerRequest("John wick", 30);
        Customer adultCustomer = new Customer("John wick", 30);

        CustomerRequest c1 = new CustomerRequest("Kid1", 5);
        CustomerRequest c2 = new CustomerRequest("Kid2", 6);
        CustomerRequest c3 = new CustomerRequest("Kid3", 8);

        Customer customer1 = new Customer("Kid1", 5);
        Customer customer2 = new Customer("Kid2", 6);
        Customer customer3 = new Customer("Kid3", 8);

        Mockito.when(mapper.mapToCustomer(Mockito.any())).thenReturn(adultCustomer).thenReturn(customer1).thenReturn(customer2).thenReturn(customer3);


        TransactionRequest request = new TransactionRequest(2L, List.of(a1, c1, c2, c3));

        TransactionResponse response = transactionService.calculateTotalCostForTransaction(request);

        assertEquals(2L, request.transactionId()); // actually 2L here
        assertEquals(2, response.tickets().size());
        TicketSummary ticket = response.tickets().get(0);
        assertEquals("Adult", ticket.ticketType());
        assertEquals(1, ticket.quantity());
        assertEquals(new BigDecimal("25.00"), ticket.totalCost());

        TicketSummary chileTicket = response.tickets().get(1);
        assertEquals("Children", chileTicket.ticketType());
        assertEquals(3, chileTicket.quantity());
        assertEquals(new BigDecimal("11.25"), chileTicket.totalCost());

        assertEquals(new BigDecimal("36.25"), response.totalCost());
    }


    @Test
    void testCalculateTotalCostForTransaction_SingleSeniorWithChildrenDiscount() {


        Mockito.when(seniorStrategy.calculatePrice(any())).thenReturn(new BigDecimal("17.50"));
        Mockito.when(seniorStrategy.getTicketType()).thenReturn(TicketType.SENIOR);
        Mockito.when(childrenStrategy.calculatePrice(any())).thenReturn(new BigDecimal("5.00"));
        Mockito.when(childrenStrategy.getTicketType()).thenReturn(TicketType.CHILDREN);


        // Inject mocked strategies into service
        transactionService = new TransactionService(
                List.of(seniorStrategy, childrenStrategy), mapper
        );

        CustomerRequest a1 = new CustomerRequest("John wick", 66);
        Customer seniorCustomer = new Customer("John wick", 66);

        CustomerRequest c1 = new CustomerRequest("Kid1", 5);
        CustomerRequest c2 = new CustomerRequest("Kid2", 6);
        CustomerRequest c3 = new CustomerRequest("Kid3", 8);

        Customer customer1 = new Customer("Kid1", 5);
        Customer customer2 = new Customer("Kid2", 6);
        Customer customer3 = new Customer("Kid3", 8);

        Mockito.when(mapper.mapToCustomer(Mockito.any())).thenReturn(seniorCustomer).thenReturn(customer1).thenReturn(customer2).thenReturn(customer3);


        TransactionRequest request = new TransactionRequest(2L, List.of(a1, c1, c2, c3));

        TransactionResponse response = transactionService.calculateTotalCostForTransaction(request);

        assertEquals(2L, request.transactionId());
        assertEquals(2, response.tickets().size());
        TicketSummary ticket = response.tickets().get(1);
        assertEquals("Senior", ticket.ticketType());
        assertEquals(1, ticket.quantity());
        assertEquals(new BigDecimal("17.50"), ticket.totalCost());

        TicketSummary chileTicket = response.tickets().get(0);
        assertEquals("Children", chileTicket.ticketType());
        assertEquals(3, chileTicket.quantity());
        assertEquals(new BigDecimal("11.25"), chileTicket.totalCost());

        assertEquals(new BigDecimal("28.75"), response.totalCost());
    }


    @Test
    void testCalculateTotalCostForTransaction_singleTeen() {

        Mockito.when(teenStrategy.calculatePrice(any())).thenReturn(new BigDecimal("12.00"));
        Mockito.when(teenStrategy.getTicketType()).thenReturn(TicketType.TEEN);

        // Inject mocked strategies into service
        transactionService = new TransactionService(
                List.of(teenStrategy), mapper
        );

        CustomerRequest cust = new CustomerRequest("John wick", 17);
        TransactionRequest request = new TransactionRequest(1L, List.of(cust));

        Customer teenCustomer = new Customer("John wick", 17);
        Mockito.when(mapper.mapToCustomer(Mockito.any())).thenReturn(teenCustomer);


        TransactionResponse response = transactionService.calculateTotalCostForTransaction(request);

        assertEquals(1L, response.transactionId());
        assertEquals(1, response.tickets().size());
        TicketSummary ticket = response.tickets().get(0);
        assertEquals("Teen", ticket.ticketType());
        assertEquals(1, ticket.quantity());
        assertEquals(new BigDecimal("12.00"), ticket.totalCost());
        assertEquals(new BigDecimal("12.00"), response.totalCost());
    }


    @Test
    void testCalculateTotalCostForTransaction_allCustomerType() {

        Mockito.when(teenStrategy.calculatePrice(any())).thenReturn(new BigDecimal("12.00"));
        Mockito.when(teenStrategy.getTicketType()).thenReturn(TicketType.TEEN);
        Mockito.when(seniorStrategy.calculatePrice(any())).thenReturn(new BigDecimal("17.50"));
        Mockito.when(seniorStrategy.getTicketType()).thenReturn(TicketType.SENIOR);
        Mockito.when(childrenStrategy.calculatePrice(any())).thenReturn(new BigDecimal("5.00"));
        Mockito.when(childrenStrategy.getTicketType()).thenReturn(TicketType.CHILDREN);
        Mockito.when(adultStrategy.calculatePrice(any())).thenReturn(new BigDecimal("25.00"));
        Mockito.when(adultStrategy.getTicketType()).thenReturn(TicketType.ADULT);

        // Inject mocked strategies into service
        transactionService = new TransactionService(
                List.of(adultStrategy, seniorStrategy, teenStrategy, childrenStrategy), mapper
        );

        CustomerRequest teenCust = new CustomerRequest("Teen wick", 17);


        Customer teenCustomer = new Customer("Teen wick", 17);


        CustomerRequest seniorCust = new CustomerRequest("Senior wick", 66);
        Customer seniorCustomer = new Customer("Senior wick", 66);

        CustomerRequest c1 = new CustomerRequest("Kid1", 5);
        CustomerRequest c2 = new CustomerRequest("Kid2", 6);
        CustomerRequest c3 = new CustomerRequest("Kid3", 8);

        Customer customer1 = new Customer("Kid1", 5);
        Customer customer2 = new Customer("Kid2", 6);
        Customer customer3 = new Customer("Kid3", 8);


        CustomerRequest adultCust = new CustomerRequest("Adult wick", 30);

        Customer adultCustomer = new Customer("Adult wick", 30);


        TransactionRequest request = new TransactionRequest(1L, List.of(teenCust, seniorCust, c1, c2, c3, adultCust));

        Mockito.when(mapper.mapToCustomer(Mockito.any())).thenReturn(teenCustomer).thenReturn(seniorCustomer)
                .thenReturn(customer1).thenReturn(customer2).thenReturn(customer3).thenReturn(adultCustomer);


        TransactionResponse response = transactionService.calculateTotalCostForTransaction(request);


        assertEquals(1L, response.transactionId());
        assertEquals(4, response.tickets().size());

        // assert childticket
        TicketSummary adultTicket = response.tickets().get(0);
        assertEquals("Adult", adultTicket.ticketType());
        assertEquals(1, adultTicket.quantity());
        assertEquals(new BigDecimal("25.00"), adultTicket.totalCost());

        //assert childTicker
        TicketSummary childticket = response.tickets().get(1);
        assertEquals("Children", childticket.ticketType());
        assertEquals(3, childticket.quantity());
        assertEquals(new BigDecimal("11.25"), childticket.totalCost());

        //assert childTicker
        TicketSummary seniorTicket = response.tickets().get(2);
        assertEquals("Senior", seniorTicket.ticketType());
        assertEquals(1, seniorTicket.quantity());
        assertEquals(new BigDecimal("17.50"), seniorTicket.totalCost());

        //assert childTicker
        TicketSummary teenTicket = response.tickets().get(3);
        assertEquals("Teen", teenTicket.ticketType());
        assertEquals(1, teenTicket.quantity());
        assertEquals(new BigDecimal("12.00"), teenTicket.totalCost());

        assertEquals(new BigDecimal("65.75"), response.totalCost());


    }


    @Test
    void testCalculateTotalCostForTransaction_invalidRequest() {

        Mockito.when(adultStrategy.calculatePrice(any())).thenReturn(null);
        Mockito.when(adultStrategy.getTicketType()).thenReturn(TicketType.ADULT);

        // Inject mocked strategies into service
        transactionService = new TransactionService(
                List.of(adultStrategy), mapper
        );


        CustomerRequest cust = new CustomerRequest("John wick", 30);
        TransactionRequest request = new TransactionRequest(1L, List.of(cust));

        Customer customer = new Customer("John wick", 30);
        Mockito.when(mapper.mapToCustomer(Mockito.any())).thenReturn(customer);

        Assertions.assertThrows(TransactionException.class, () -> transactionService.calculateTotalCostForTransaction(request));
    }


}

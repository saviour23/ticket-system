package com.au.sportsbet.ticket.system.api.controller;

import com.au.sportsbet.ticket.system.api.dto.CustomerRequest;
import com.au.sportsbet.ticket.system.api.dto.TicketSummary;
import com.au.sportsbet.ticket.system.api.dto.TransactionRequest;
import com.au.sportsbet.ticket.system.api.dto.TransactionResponse;
import com.au.sportsbet.ticket.system.api.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TicketTransactionController.class)
class TicketTransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TransactionService transactionService;

    @Autowired
    private ObjectMapper objectMapper;

    private TransactionRequest request;
    private TransactionResponse response;

    @Test
    void testProcessTransaction_success() throws Exception {

        CustomerRequest customer = new CustomerRequest("John Wick", 30);

        request = new TransactionRequest(1L, List.of(customer));

        TicketSummary summary = new TicketSummary("Adult", 1, BigDecimal.valueOf(10.00));
        response = new TransactionResponse(1L, List.of(summary), BigDecimal.valueOf(10.00));
        Mockito.when(transactionService.calculateTotalCostForTransaction(Mockito.any(TransactionRequest.class)))
                .thenReturn(response);

        mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.transactionId").value(1L))
                .andExpect(jsonPath("$.totalCost").value(10.00))
        ;
    }


    @Test
    void testProcessTransaction_invalidRequest() throws Exception {

        CustomerRequest customer = new CustomerRequest("John Wick", -1);

        request = new TransactionRequest(1L, List.of(customer));

        Mockito.when(transactionService.calculateTotalCostForTransaction(Mockito.any(TransactionRequest.class)))
                .thenReturn(response);

        MvcResult result = mockMvc.perform(post("/api/transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is4xxClientError())
                .andExpect(content()
                        .contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn();
        String jsonResponse = result.getResponse().getContentAsString();
        Assertions.assertEquals("{\"customers[0].age\":\"Age must be 0 or greater\"}", jsonResponse);


    }
}

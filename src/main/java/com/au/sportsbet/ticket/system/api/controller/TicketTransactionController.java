package com.au.sportsbet.ticket.system.api.controller;

import com.au.sportsbet.ticket.system.api.constants.ApiConstant;
import com.au.sportsbet.ticket.system.api.dto.TransactionRequest;
import com.au.sportsbet.ticket.system.api.dto.TransactionResponse;
import com.au.sportsbet.ticket.system.api.service.TransactionService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Rest controller class to handle the API request for transaction calculation.
 */
@Slf4j
@RestController
public class TicketTransactionController {

    private final TransactionService transactionService;

    @Autowired
    public TicketTransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping
    @RequestMapping(ApiConstant.TRANSACTION_ENDPOINT)
    public TransactionResponse process(@Valid @RequestBody TransactionRequest request) {

        boolean isVirtual = Thread.currentThread().isVirtual();
        log.info("Received transactions request in controller, running on virtualThread = {}", isVirtual);

        return transactionService.calculateTotalCostForTransaction(request);
    }
}

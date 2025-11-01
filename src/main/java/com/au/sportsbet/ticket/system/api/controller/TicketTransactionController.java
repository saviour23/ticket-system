package com.au.sportsbet.ticket.system.api.controller;

import com.au.sportsbet.ticket.system.api.dto.TransactionRequest;
import com.au.sportsbet.ticket.system.api.dto.TransactionResponse;
import com.au.sportsbet.ticket.system.api.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TicketTransactionController {

  private final TransactionService transactionService;

  @Autowired
  public TicketTransactionController(TransactionService transactionService) {
    this.transactionService = transactionService;
  }

  @PostMapping
  @RequestMapping("/api/transactions")
  public TransactionResponse process(@Valid @RequestBody TransactionRequest request) {
    return transactionService.processTransaction(request);
  }
}

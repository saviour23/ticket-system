package com.au.sb.ticket.system.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * Request class for Transactions.
 *
 * @param transactionId
 * @param customers
 */
public record TransactionRequest(
        @NotNull(message = "Transaction ID is mandatory") Long transactionId,
        @NotEmpty(message = "Customers list cannot be empty")
        @Valid List<CustomerRequest> customers) {
}

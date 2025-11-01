package com.au.sportsbet.ticket.system.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.List;

/**
 * Response class for Transaction endpoint.
 *
 * @param transactionId
 * @param tickets
 * @param totalCost
 */
public record TransactionResponse(
        Long transactionId,
        List<TicketSummary> tickets,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.00") BigDecimal totalCost) {
}

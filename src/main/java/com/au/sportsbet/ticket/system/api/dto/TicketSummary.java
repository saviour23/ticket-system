package com.au.sportsbet.ticket.system.api.dto;

import com.au.sportsbet.ticket.system.api.constants.ApiConstant;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;

public record TicketSummary(
        String ticketType,
        Integer quantity,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = ApiConstant.DECIMAL_FORMAT) BigDecimal totalCost) {
}


package com.au.sportsbet.ticket.system.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

public record TicketSummary(
     String ticketType,
     Integer quantity,
     @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "0.00") BigDecimal totalCost){}


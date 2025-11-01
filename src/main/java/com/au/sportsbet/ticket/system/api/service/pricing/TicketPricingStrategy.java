package com.au.sportsbet.ticket.system.api.service.pricing;

import com.au.sportsbet.ticket.system.api.model.Customer;
import com.au.sportsbet.ticket.system.api.model.TicketType;

import java.math.BigDecimal;

/**
 * Ticket price interface, following the Strategy design pattern.
 */
public interface TicketPricingStrategy {
  TicketType getTicketType();

  BigDecimal calculatePrice(Customer customer);
}

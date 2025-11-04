package com.au.sb.ticket.system.api.service.pricing;

import com.au.sb.ticket.system.api.model.Customer;
import com.au.sb.ticket.system.api.model.TicketType;

import java.math.BigDecimal;

/**
 * Ticket price interface, following the Strategy design pattern.
 */
public interface TicketPricingStrategy {
    /**
     * Method to get the Ticket Type
     *
     * @return {@link TicketType}
     */
    TicketType getTicketType();

    /**
     * Method to calculate the price for each customer type.
     *
     * @param customer {@link Customer}
     * @return calculated price.
     */
    BigDecimal calculatePrice(Customer customer);
}

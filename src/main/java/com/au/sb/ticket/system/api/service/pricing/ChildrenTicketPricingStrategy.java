package com.au.sb.ticket.system.api.service.pricing;

import com.au.sb.ticket.system.api.model.Customer;
import com.au.sb.ticket.system.api.model.TicketType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Pricing strategy class for Children.
 */
@Component
public class ChildrenTicketPricingStrategy implements TicketPricingStrategy {
    private static final BigDecimal PRICE = BigDecimal.valueOf(5.00);

    @Override
    public TicketType getTicketType() {
        return TicketType.CHILDREN;
    }

    public BigDecimal calculatePrice(Customer c) {
        return PRICE;
    }
}

package com.au.sportsbet.ticket.system.api.service.pricing;

import com.au.sportsbet.ticket.system.api.model.Customer;
import com.au.sportsbet.ticket.system.api.model.TicketType;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Pricing strategy class for Senior.
 */
@Component
public class SeniorTicketPricingStrategy implements TicketPricingStrategy {

    private static final BigDecimal DISCOUNT = BigDecimal.valueOf(0.7);
    private static final BigDecimal BASE_PRICE = BigDecimal.valueOf(25.00);

    @Override
    public TicketType getTicketType() {
        return TicketType.SENIOR;
    }

    @Override
    public BigDecimal calculatePrice(Customer c) {
        return BASE_PRICE.multiply(DISCOUNT).setScale(2, RoundingMode.HALF_UP);
    }
}

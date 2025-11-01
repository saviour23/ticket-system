package com.au.sportsbet.ticket.system.api.model;

/**
 * Model class for customer.
 * @param name
 * @param age
 */
public record Customer(
        String name,
        Integer age) {
}

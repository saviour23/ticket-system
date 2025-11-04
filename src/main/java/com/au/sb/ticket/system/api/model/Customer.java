package com.au.sb.ticket.system.api.model;

/**
 * Model class for customer.
 * @param name
 * @param age
 */
public record Customer(
        String name,
        Integer age) {
}

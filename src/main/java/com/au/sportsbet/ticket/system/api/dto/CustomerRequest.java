package com.au.sportsbet.ticket.system.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Customer Request class
 *
 * @param name
 * @param age
 */
public record CustomerRequest(
        @NotBlank(message = "Customer name is mandatory") String name,
        @NotNull(message = "Age is mandatory") @Min(value = 0, message = "Age must be 0 or greater") Integer age) {
}


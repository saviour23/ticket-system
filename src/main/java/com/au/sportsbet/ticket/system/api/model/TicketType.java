package com.au.sportsbet.ticket.system.api.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * enum for the Ticket Types.
 */
@Getter
@AllArgsConstructor
public enum TicketType {
    ADULT("Adult"),
    SENIOR("Senior"),
    TEEN("Teen"),
    CHILDREN("Children");

    final String name;

}

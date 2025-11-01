package com.au.sportsbet.ticket.system.api.mapper;


import com.au.sportsbet.ticket.system.api.dto.CustomerRequest;
import com.au.sportsbet.ticket.system.api.model.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

/**
 * Mapper interface to map between DTO and model objects.
 */
@Mapper(componentModel = "spring")
public interface ToCustomerMapper {

    Customer mapToCustomer(CustomerRequest customerRequest);

}

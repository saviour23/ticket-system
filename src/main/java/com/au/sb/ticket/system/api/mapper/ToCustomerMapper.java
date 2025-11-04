package com.au.sb.ticket.system.api.mapper;


import com.au.sb.ticket.system.api.dto.CustomerRequest;
import com.au.sb.ticket.system.api.model.Customer;
import org.mapstruct.Mapper;

/**
 * Mapper interface to map between DTO and model objects.
 */
@Mapper(componentModel = "spring")
public interface ToCustomerMapper {

    Customer mapToCustomer(CustomerRequest customerRequest);

}

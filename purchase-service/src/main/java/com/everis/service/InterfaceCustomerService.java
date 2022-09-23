package com.everis.service;

import com.everis.model.Customer;
import reactor.core.publisher.Mono;

/**
 * Interface de Metodos del Customer.
 */
public interface InterfaceCustomerService extends InterfaceCrudService<Customer, String> {

  Mono<Customer> findByIdentityNumber(String identityNumber);

}

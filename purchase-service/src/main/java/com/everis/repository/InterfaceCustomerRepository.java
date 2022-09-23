package com.everis.repository;

import com.everis.model.Customer;
import reactor.core.publisher.Mono;

/**
 * Interface de Metodos del Repositorio.
 */
public interface InterfaceCustomerRepository extends InterfaceRepository<Customer, String> {

  Mono<Customer> findByIdentityNumber(String identityNumber);

}

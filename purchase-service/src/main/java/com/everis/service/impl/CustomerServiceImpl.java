package com.everis.service.impl;

import com.everis.model.Customer;
import com.everis.repository.InterfaceCustomerRepository;
import com.everis.repository.InterfaceRepository;
import com.everis.service.InterfaceCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Implementacion de los Metodos del Cliente.
 */
@Service
public class CustomerServiceImpl extends CrudServiceImpl<Customer, String>
        implements InterfaceCustomerService {

  @Autowired
  private InterfaceCustomerRepository repository;

  @Override
  protected InterfaceRepository<Customer, String> getRepository() {

    return repository;

  }

  @Override
  public Mono<Customer> findByIdentityNumber(String identityNumber) {

    return repository.findByIdentityNumber(identityNumber);

  }

}

package com.everis.service;

import com.everis.model.Deposit;
import java.util.List;
import reactor.core.publisher.Mono;

/**
 * Interface de Metodos del Service Deposit.
 */
public interface InterfaceDepositService extends InterfaceCrudService<Deposit, String> {
  
  Mono<List<Deposit>> findAllDeposit();
  
  Mono<Deposit> createDeposit(Deposit deposit);
  
}

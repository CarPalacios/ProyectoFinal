package com.everis.service;

import com.everis.model.Withdrawal;
import java.util.List;
import reactor.core.publisher.Mono;

/**
 * Interface de Metodos del Withdrawal.
 */
public interface InterfaceWithdrawalService extends InterfaceCrudService<Withdrawal, String> {
  
  Mono<List<Withdrawal>> findAllWithdrawal();
  
  Mono<Withdrawal> createWithdrawal(Withdrawal withdrawal);

}

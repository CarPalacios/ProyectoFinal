package com.everis.repository;

import com.everis.model.Wallet;
import reactor.core.publisher.Mono;

/**
 * Interface de Metodos del Repositorio.
 */
public interface InterfaceWalletRepository extends InterfaceRepository<Wallet, String> {

  Mono<Wallet> findByPhoneNumber(String phoneNumber);

}

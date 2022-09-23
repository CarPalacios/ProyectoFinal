package com.everis.repository;

import com.everis.model.Coin;

import reactor.core.publisher.Mono;

/**
 * Interface de Metodos del Repositorio.
 */
public interface InterfaceCoinRepository extends InterfaceRepository<Coin, String> {

  Mono<Coin> findByIdentityNumber(String identityNumber);
  
}

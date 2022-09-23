package com.everis.repository;

import com.everis.model.Request;

import reactor.core.publisher.Mono;

/**
 * Interface de Metodos del Repositorio.
 */
public interface InterfaceRequestRepository extends InterfaceRepository<Request, String> {

  Mono<Request> findByNumber(String number);
  
}

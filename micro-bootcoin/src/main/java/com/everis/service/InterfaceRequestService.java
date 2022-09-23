package com.everis.service;

import com.everis.model.Request;

import reactor.core.publisher.Mono;

/**
 * Interface de Metodos del Service Coin.
 */
public interface InterfaceRequestService extends InterfaceCrudService<Request, String> {
  
  Mono<Request> findByNumber(String number);
  
}

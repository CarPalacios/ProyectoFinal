package com.everis.service;

import com.everis.model.ExchangeRate;

import reactor.core.publisher.Mono;

/**
 * Interface de Metodos del Service Coin.
 */
public interface InterfaceExchangeRateService extends InterfaceCrudService<ExchangeRate, String> {
  
  Mono<ExchangeRate> updateRate(ExchangeRate exchangeRate, String id);
  
}

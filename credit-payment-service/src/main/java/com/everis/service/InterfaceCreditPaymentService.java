package com.everis.service;

import com.everis.model.CreditPayment;
import java.util.List;
import reactor.core.publisher.Mono;

/**
 * Interface de Metodos del Credit Payment.
 */
public interface InterfaceCreditPaymentService extends InterfaceCrudService<CreditPayment, String> {

  Mono<List<CreditPayment>> findAllPayment();

  Mono<CreditPayment> createPayment(CreditPayment creditPayment);
  
}

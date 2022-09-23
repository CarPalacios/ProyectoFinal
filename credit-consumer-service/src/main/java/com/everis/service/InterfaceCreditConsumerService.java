package com.everis.service;

import com.everis.model.CreditConsumer;
import java.util.List;
import reactor.core.publisher.Mono;

/**
 * Interface de Metodos del Credit Consumer.
 */
public interface InterfaceCreditConsumerService 
    extends InterfaceCrudService<CreditConsumer, String> {
  
  Mono<List<CreditConsumer>> findAllCredit();
  
  Mono<CreditConsumer> createCredit(CreditConsumer creditConsumer);
  
}

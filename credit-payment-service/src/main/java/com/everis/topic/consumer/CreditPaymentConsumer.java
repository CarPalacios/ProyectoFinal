package com.everis.topic.consumer;

import com.everis.model.Purchase;
import com.everis.service.InterfacePurchaseService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

/**
 * Clase Consumidor de Topicos.
 */
@Component
public class CreditPaymentConsumer {
  
  @Autowired
  private InterfacePurchaseService purchaseService;
  
  ObjectMapper objectMapper = new ObjectMapper();
  
  /** Consume del topico purchase. */
  @KafkaListener(topics = "created-purchase-topic", groupId = "credit-payment-group")
  public Disposable retrieveCreatedPurchase(String data) throws JsonProcessingException {
  
    Purchase purchase = objectMapper.readValue(data, Purchase.class);
    
    if (purchase.getProduct().getProductType().equals("PASIVO")) {
      
      return null;
        
    }
    
    return Mono.just(purchase)
      .log()
      .flatMap(purchaseService::update)
      .subscribe();
  
  }
  
}

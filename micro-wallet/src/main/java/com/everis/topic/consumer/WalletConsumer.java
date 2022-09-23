package com.everis.topic.consumer;

import com.everis.model.Account;
import com.everis.model.Purchase;
import com.everis.service.InterfaceAccountService;
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
public class WalletConsumer {
  
  @Autowired
  private InterfacePurchaseService purchaseService;

  @Autowired
  private InterfaceAccountService accountService;
  
  ObjectMapper objectMapper = new ObjectMapper();
  
  /** Consume del topico purchase. */
  @KafkaListener(topics = "created-purchase-topic", groupId = "wallet-group")
  public Disposable retrieveCreatedPurchase(String data) throws JsonProcessingException {
  
    Purchase purchase = objectMapper.readValue(data, Purchase.class);
    
    if (!purchase.getProduct().getProductType().equalsIgnoreCase("PASIVO")) {
    
      return null;
        
    }
    
    return Mono.just(purchase)
      .log()
      .flatMap(purchaseService::update)
      .subscribe();
      
  }
  
  /** Consume del topico account. */
  @KafkaListener(topics = "created-account-topic", groupId = "wallet-group")
  public Disposable retrieveCreatedAccount(String data) throws JsonProcessingException {
  
    Account account = objectMapper.readValue(data, Account.class);
      
    return Mono.just(account)
      .log()
      .flatMap(accountService::update)
      .subscribe();
  
  }
  
}

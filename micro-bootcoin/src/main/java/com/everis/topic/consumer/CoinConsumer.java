package com.everis.topic.consumer;

import com.everis.model.Account;
import com.everis.model.Wallet;
import com.everis.service.InterfaceAccountService;
import com.everis.service.InterfaceWalletService;
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
public class CoinConsumer {
  
  @Autowired
  private InterfaceWalletService walletService;

  @Autowired
  private InterfaceAccountService accountService;
  
  ObjectMapper objectMapper = new ObjectMapper();
  
  /** Consume del topico purchase. */
  @KafkaListener(topics = "created-wallet-topic", groupId = "coin-group")
  public Disposable retrieveCreatedWallet(String data) throws JsonProcessingException {
  
    Wallet wallet = objectMapper.readValue(data, Wallet.class);
        
    return Mono.just(wallet)
      .log()
      .flatMap(walletService::update)
      .subscribe();
      
  }
  
  /** Consume del topico account. */
  @KafkaListener(topics = "created-account-topic", groupId = "coin-group")
  public Disposable retrieveCreatedAccount(String data) throws JsonProcessingException {
  
    Account account = objectMapper.readValue(data, Account.class);
      
    if (!account.getPurchase().getProduct().getProductName().equalsIgnoreCase("AHORRO")
        && !account.getPurchase().getProduct().getProductName().equalsIgnoreCase("CUENTA CORRIENTE")) {
      
      return null;
        
    }
    
    return Mono.just(account)
      .log()
      .flatMap(accountService::update)
      .subscribe();
  
  }
  
}

package com.everis.service.impl;

import com.everis.model.CreditConsumer;
import com.everis.model.Purchase;
import com.everis.repository.InterfaceCreditConsumerRepository;
import com.everis.repository.InterfaceRepository;
import com.everis.service.InterfaceCreditConsumerService;
import com.everis.service.InterfacePurchaseService;
import com.everis.topic.producer.CreditConsumerProducer;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Implementacion de Metodos del Service Credit Consumer.
 */
@Slf4j
@Service
public class CreditConsumerServiceImpl extends CrudServiceImpl<CreditConsumer, String> 
    implements InterfaceCreditConsumerService {

  static final String CIRCUIT = "creditConsumerServiceCircuitBreaker";
  
  @Value("${msg.error.registro.notfound.all}")
  private String msgNotFoundAll;
  
  @Value("${msg.error.registro.card.exists}")
  private String msgCardNotExists;
  
  @Value("${msg.error.registro.positive}")
  private String msgPositive;
  
  @Value("${msg.error.registro.exceed}")
  private String msgExceed;
  
  @Value("${msg.error.registro.notfound.create}")
  private String msgNotFoundCreate;
  
  @Autowired
  private InterfaceCreditConsumerRepository repository;
  
  @Autowired
  private InterfaceCreditConsumerService service;
  
  @Autowired
  private InterfacePurchaseService purchaseService;
  
  @Autowired
  private CreditConsumerProducer producer;
  
  @Override
  protected InterfaceRepository<CreditConsumer, String> getRepository() {
  
    return repository;
  
  }
  
  @Override
  @CircuitBreaker(name = CIRCUIT, fallbackMethod = "findAllFallback")
  public Mono<List<CreditConsumer>> findAllCredit() {
    
    Flux<CreditConsumer> creditDatabase = service.findAll()
        .switchIfEmpty(Mono.error(new RuntimeException(msgNotFoundAll)));
    
    return creditDatabase.collectList().flatMap(Mono::just);
    
  }

  @Override
  @CircuitBreaker(name = CIRCUIT, fallbackMethod = "createFallback")
  public Mono<CreditConsumer> createCredit(CreditConsumer creditConsumer) {
    
    Mono<Purchase> purchaseDatabase = purchaseService
        .findByCardNumber(creditConsumer.getPurchase().getCardNumber())
        .switchIfEmpty(Mono.error(new RuntimeException(msgCardNotExists)));
    
    return purchaseDatabase
        .flatMap(purchase -> {
                    
          if (creditConsumer.getAmount() < 0) {
            
            return Mono.error(new RuntimeException(msgPositive));
            
          }
          
          if (creditConsumer.getAmount() > purchase.getAmountFin()) {
            
            return Mono.error(new RuntimeException(msgExceed));
            
          }
          
          purchase.setAmountFin(purchase.getAmountFin() - creditConsumer.getAmount());
          creditConsumer.setPurchase(purchase);
          creditConsumer.setConsumDate(LocalDateTime.now());
          
          return service.create(creditConsumer)
              .map(createdObject -> {
                            
                producer.sendCreditConsumerTransactionTopic(creditConsumer);              
                return createdObject;
                          
              })
              .switchIfEmpty(Mono.error(new RuntimeException(msgNotFoundCreate)));
                  
        });
      
  }
  
  /** Mensaje si no existen consumos de credito. */
  public Mono<List<CreditConsumer>> findAllFallback(Exception ex) {
    
    log.info("Consumos de credito no encontrados, retornando fallback");
  
    List<CreditConsumer> list = new ArrayList<>();
    
    list.add(CreditConsumer
        .builder()
        .id(ex.getMessage())
        .build());
    
    return Mono.just(list);
    
  }
  
  /** Mensaje si falla el create. */
  public Mono<CreditConsumer> createFallback(CreditConsumer creditConsumer, Exception ex) {
  
    log.info("Consumos de credito con numero de tarjeta {} no se pudo crear, "
        + "retornando fallback", creditConsumer.getPurchase().getCardNumber());
  
    return Mono.just(CreditConsumer
        .builder()
        .id(ex.getMessage())
        .description(creditConsumer.getPurchase().getCardNumber())
        .build());
    
  }
  
}

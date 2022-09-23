package com.everis.service.impl;

import com.everis.model.CreditPayment;
import com.everis.model.Purchase;
import com.everis.repository.InterfaceCreditPaymentRepository;
import com.everis.repository.InterfaceRepository;
import com.everis.service.InterfaceCreditPaymentService;
import com.everis.service.InterfacePurchaseService;
import com.everis.topic.producer.CreditPaymentProducer;
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
public class CreditPaymentServiceImpl extends CrudServiceImpl<CreditPayment, String> 
    implements InterfaceCreditPaymentService {

  static final String CIRCUIT = "creditPaymentServiceCircuitBreaker";
  
  @Value("${msg.error.registro.notfound.all}")
  private String msgNotFoundAll;

  @Value("${msg.error.registro.card.exists}")
  private String msgCardNotExists;

  @Value("${msg.error.registro.positive}")
  private String msgPositive;

  @Value("${msg.error.registro.minimum}")
  private String msgMinimum;

  @Value("${msg.error.registro.notfound.create}")
  private String msgNotFoundCreate;
  
  @Autowired
  private InterfaceCreditPaymentRepository repository;
  
  @Autowired
  private InterfaceCreditPaymentService service;
  
  @Autowired
  private InterfacePurchaseService purchaseService;
  
  @Autowired
  private CreditPaymentProducer producer;
  
  @Override
  protected InterfaceRepository<CreditPayment, String> getRepository() {
  
    return repository;
  
  }
  
  @Override
  @CircuitBreaker(name = CIRCUIT, fallbackMethod = "findAllFallback")
  public Mono<List<CreditPayment>> findAllPayment() {
    
    Flux<CreditPayment> paymentDatabase = service.findAll()
        .switchIfEmpty(Mono.error(new RuntimeException(msgNotFoundAll)));
    
    return paymentDatabase.collectList().flatMap(Mono::just);
    
  }
  
  @Override
  @CircuitBreaker(name = CIRCUIT, fallbackMethod = "createFallback")
  public Mono<CreditPayment> createPayment(CreditPayment creditPayment) {
    
    Mono<Purchase> purchaseDatabase = purchaseService
        .findByCardNumber(creditPayment.getPurchase().getCardNumber())
        .switchIfEmpty(Mono.error(new RuntimeException(msgCardNotExists)));
    
    return purchaseDatabase
        .flatMap(purchase -> {
        
          Double monto = purchase.getAmountIni() - purchase.getAmountFin();
          
          if (creditPayment.getAmount() < 0) {
            
            return Mono.error(new RuntimeException(msgPositive));
            
          }
          
          if (creditPayment.getAmount() < monto) {
            
            return Mono.error(new RuntimeException(msgMinimum + ": " + monto));
            
          }
                
          creditPayment.setAmount(purchase.getAmountIni() - purchase.getAmountFin());
          purchase.setAmountFin(purchase.getAmountIni());
          creditPayment.setPurchase(purchase);
          creditPayment.setPaymentDate(LocalDateTime.now());
          
          return service.create(creditPayment)
              .map(createdObject -> {
                
                producer.sendCreditPaymentTransactionTopic(creditPayment);                
                return creditPayment;

              })
              .switchIfEmpty(Mono.error(new RuntimeException(msgNotFoundCreate)));
      
        });
    
  }
  
  /** Mensaje si no existen pagos de credito. */
  public Mono<List<CreditPayment>> findAllFallback(Exception ex) {
    
    log.info("Pagos de credito no encontrados, retornando fallback");
  
    List<CreditPayment> list = new ArrayList<>();
    
    list.add(CreditPayment
        .builder()
        .id(ex.getMessage())
        .build());
    
    return Mono.just(list);
    
  }
  
  /** Mensaje si falla el create. */
  public Mono<CreditPayment> createFallback(CreditPayment creditPayment, Exception ex) {
  
    log.info("Pagos de credito con numero de tarjeta {} no se pudo crear, "
        + "retornando fallback", creditPayment.getPurchase().getCardNumber());
  
    return Mono.just(CreditPayment
        .builder()
        .id(ex.getMessage())
        .description(creditPayment.getPurchase().getCardNumber())
        .build());
    
  }
  
}
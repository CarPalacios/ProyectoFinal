package com.everis.service.impl;

import com.everis.model.Wallet;
import com.everis.repository.InterfaceRepository;
import com.everis.repository.InterfaceWalletRepository;
import com.everis.service.InterfaceWalletService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Implementacion de Metodos del Service Wallet.
 */
@Slf4j
@Service
public class WalletServiceImpl extends CrudServiceImpl<Wallet, String>
    implements InterfaceWalletService {

  static final String CIRCUIT = "walletServiceCircuitBreaker";

//  @Value("${msg.error.registro.notfound.all}")
//  private String msgNotFoundAll;

  @Value("${msg.error.registro.notfound}")
  private String msgNotFound;

//  @Value("${msg.error.registro.if.exists}")
//  private String msgIfExists;
//
//  @Value("${msg.error.registro.card.exists}")
//  private String msgCardNotExists;
//
//  @Value("${msg.error.registro.notfound.create}")
//  private String msgNotFoundCreate;
//
//  @Value("${msg.error.registro.notfound.update}")
//  private String msgNotFoundUpdate;
//
//  @Value("${msg.error.registro.notfound.delete}")
//  private String msgNotFoundDelete;
//
//  @Value("${msg.error.registro.wallet.delete}")
//  private String msgWalletDelete;

  @Autowired
  private InterfaceWalletRepository repository;

//  @Autowired
//  private InterfaceWalletService service;
//  
//  @Autowired
//  private InterfacePurchaseService purchaseService;
//
//  @Autowired
//  private WalletProducer producer;

  @Override
  protected InterfaceRepository<Wallet, String> getRepository() {

    return repository;

  }

//  @Override
//  @CircuitBreaker(name = CIRCUIT, fallbackMethod = "findAllFallback")
//  public Mono<List<Wallet>> findAllWallet() {
//
//    Flux<Wallet> walletDatabase = service.findAll()
//        .switchIfEmpty(Mono.error(new RuntimeException(msgNotFoundAll)));
//
//    return walletDatabase.collectList().flatMap(Mono::just);
//
//  }

  @Override
  @CircuitBreaker(name = CIRCUIT, fallbackMethod = "walletFallback")
  public Mono<Wallet> findByPhoneNumber(String phoneNumber) {

    return repository.findByPhoneNumber(phoneNumber)
        .switchIfEmpty(Mono.error(new RuntimeException(msgNotFound)));

  }

//  @Override
//  @CircuitBreaker(name = CIRCUIT, fallbackMethod = "createFallback")
//  public Mono<Wallet> createWallet(Wallet wallet) {
//
//    Mono<Wallet> monoWallet = Mono.just(wallet.toBuilder().build());    
//    
//    Flux<Wallet> walletDatabase = service.findAll()
//        .filter(list -> list.getPhoneNumber().equals(wallet.getPhoneNumber()));
//    
//    Mono<Purchase> purchaseDatabase = purchaseService.findByCardNumber(wallet.getPurchase().getCardNumber())
//        .switchIfEmpty(Mono.error(new RuntimeException(msgCardNotExists)));
//
//    return walletDatabase
//        .collectList()        
//        .flatMap(list -> {
//
//          if (list.size() > 0) {
//
//            return Mono.error(new RuntimeException(msgIfExists));
//
//          }
//
//          return monoWallet
//              .zipWith(purchaseDatabase, (a, b) -> {
//                
//                a.setPurchase(b);
//                return a;
//                
//              })
//              .flatMap(w -> service.create(w)
//                  .map(createdObject -> {
//
//                    producer.sendCreatedWalletTopic(wallet);
//                    return createdObject;
//                
//              }))
//              .switchIfEmpty(Mono.error(new RuntimeException(msgNotFoundCreate)));
//
//        });
//
//  }
//
//  @Override
//  @CircuitBreaker(name = CIRCUIT, fallbackMethod = "updateFallback")
//  public Mono<Wallet> updateWallet(Wallet wallet, String phoneNumber) {
//
//    Mono<Wallet> walletModification = Mono.just(wallet);
//
//    Mono<Wallet> walletDatabase = findByPhoneNumber(phoneNumber);
//
//    return walletDatabase
//        .zipWith(walletModification, (a, b) -> {
//
//          if (b.getName() != null) a.setName(b.getName());
//          if (b.getPhoneNumber() != null) a.setPhoneNumber(b.getPhoneNumber());
//          if (b.getImei() != null) a.setImei(b.getImei());
//          if (b.getEmail() != null) a.setEmail(b.getEmail());
//
//          return a;
//
//        })
//        .flatMap(service::update)
//        .map(objectUpdated -> {
//
//          producer.sendCreatedWalletTopic(objectUpdated);
//          return objectUpdated;
//
//        })
//        .switchIfEmpty(Mono.error(new RuntimeException(msgNotFoundUpdate)));
//
//  }
//
//  @Override
//  @CircuitBreaker(name = CIRCUIT, fallbackMethod = "deleteFallback")
//  public Mono<Response> deleteWallet(String phoneNumber) {
//
//    Mono<Wallet> walletDatabase = findByPhoneNumber(phoneNumber);
//
//    return walletDatabase
//        .flatMap(objectDelete -> service.delete(objectDelete.getId())
//            .then(Mono.just(Response.builder().data(msgWalletDelete).build())))
//        .switchIfEmpty(Mono.error(new RuntimeException(msgNotFoundDelete)));
//
//  }

//  /** Mensaje si no existen monederos. */
//  public Mono<List<Wallet>> findAllFallback(Exception ex) {
//
//    log.info("Monederos no encontrados.");
//
//    List<Wallet> list = new ArrayList<>();
//
//    list.add(Wallet
//        .builder()
//        .name(ex.getMessage())
//        .build());
//
//    return Mono.just(list);
//
//  }

  /** Mensaje si no encuentra el monedero. */
  public Mono<Wallet> walletFallback(String phoneNumber, Exception ex) {

    log.info("Monedero con numero de telefono {} no encontrado.", phoneNumber);

    return Mono.just(Wallet
        .builder()
        .phoneNumber(phoneNumber)
        .name(ex.getMessage())
        .build());

  }

//  /** Mensaje si falla el create. */
//  public Mono<Wallet> createFallback(Wallet wallet, Exception ex) {
//
//    log.info("Monedero con numero de telefono {} no se pudo crear.", wallet.getPhoneNumber());
//
//    return Mono.just(Wallet
//        .builder()
//        .phoneNumber(wallet.getPhoneNumber())
//        .name(ex.getMessage())
//        .build());
//
//  }
//
//  /** Mensaje si falla el update. */
//  public Mono<Wallet> updateFallback(Wallet wallet,
//      String phoneNumber, Exception ex) {
//
//    log.info("Monedero con numero de telefono {} no encontrado para actualizar.",
//        wallet.getPhoneNumber());
//
//    return Mono.just(Wallet
//        .builder()
//        .phoneNumber(phoneNumber)
//        .name(ex.getMessage())
//        .build());
//
//  }
//
//  /** Mensaje si falla el delete. */
//  public Mono<Response> deleteFallback(String phoneNumber, Exception ex) {
//
//    log.info("Monedero con numero de telefono {} no encontrado para eliminar.", phoneNumber);
//
//    return Mono.just(Response
//        .builder()
//        .data(phoneNumber)
//        .error(ex.getMessage())
//        .build());
//
//  }

}

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

  @Value("${msg.error.registro.notfound}")
  private String msgNotFound;

  @Autowired
  private InterfaceWalletRepository repository;

  @Override
  protected InterfaceRepository<Wallet, String> getRepository() {

    return repository;

  }


  @Override
  @CircuitBreaker(name = CIRCUIT, fallbackMethod = "walletFallback")
  public Mono<Wallet> findByPhoneNumber(String phoneNumber) {

    return repository.findByPhoneNumber(phoneNumber)
        .switchIfEmpty(Mono.error(new RuntimeException(msgNotFound)));

  }


  /** Mensaje si no encuentra el monedero. */
  public Mono<Wallet> walletFallback(String phoneNumber, Exception ex) {

    log.info("Monedero con numero de telefono {} no encontrado.", phoneNumber);

    return Mono.just(Wallet
        .builder()
        .phoneNumber(phoneNumber)
        .name(ex.getMessage())
        .build());

  }

}

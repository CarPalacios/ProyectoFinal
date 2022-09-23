package com.everis.service.impl;

import com.everis.model.Coin;
import com.everis.repository.InterfaceCoinRepository;
import com.everis.repository.InterfaceRepository;
import com.everis.service.InterfaceCoinService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Implementacion de Metodos del Service Wallet.
 */
@Slf4j
@Service
public class CoinServiceImpl extends CrudServiceImpl<Coin, String>
    implements InterfaceCoinService {

  static final String CIRCUIT = "walletServiceCircuitBreaker";

  @Value("${msg.error.registro.notfound.all}")
  private String msgNotFoundAll;

  @Value("${msg.error.registro.if.exists}")
  private String msgIfExists;

  @Autowired
  private InterfaceCoinRepository repository;

  @Override
  protected InterfaceRepository<Coin, String> getRepository() {

    return repository;

  }

  @Override
  @CircuitBreaker(name = CIRCUIT, fallbackMethod = "findAllFallback")
  public Flux<Coin> findAll() {

    Flux<Coin> coinDatabase = repository.findAll()
        .switchIfEmpty(Mono.error(new RuntimeException(msgNotFoundAll)));

    return coinDatabase.flatMap(Flux::just);

  }

  @Override
  @CircuitBreaker(name = CIRCUIT, fallbackMethod = "createFallback")
  public Mono<Coin> create(Coin coin) {

    Flux<Coin> coinDatabase = repository.findAll()
        .filter(list -> list.getIdentityNumber().equalsIgnoreCase(coin.getIdentityNumber()));
        
    return coinDatabase
        .collectList()
        .flatMap(list -> {
          
          if (list.size() > 0) {
            
            return Mono.error(new RuntimeException(msgIfExists));

          }
          
          coin.setAmount(0.0);
          
          return repository.save(coin).map(createdObject -> createdObject);
          
        });

  }

  /** Mensaje si no existen monederos. */
  public Flux<List<Coin>> findAllFallback(Exception ex) {

    log.info("Monederos no encontrados.");

    List<Coin> list = new ArrayList<>();

    list.add(Coin
        .builder()
        .id(ex.getMessage())
        .build());

    return Flux.just(list);

  }

  /** Mensaje si falla el create. */
  public Mono<Coin> createFallback(Coin coin, Exception ex) {

    log.info("Monedero con numero de identidad {} no se pudo crear.", coin.getIdentityNumber());

    return Mono.just(Coin
        .builder()
        .identityNumber(coin.getIdentityNumber())
        .id(ex.getMessage())
        .build());

  }

}

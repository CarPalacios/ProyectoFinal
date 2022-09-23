package com.everis.service.impl;

import com.everis.model.ExchangeRate;
import com.everis.repository.InterfaceExchangeRateRepository;
import com.everis.repository.InterfaceRepository;
import com.everis.service.InterfaceExchangeRateService;

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
 * Implementacion de Metodos del Service Wallet.
 */
@Slf4j
@Service
public class ExchangeRateServiceImpl extends CrudServiceImpl<ExchangeRate, String>
    implements InterfaceExchangeRateService {

  static final String CIRCUIT = "walletServiceCircuitBreaker";

  @Value("${msg.error.registro.rate.notfound.all}")
  private String msgRateNotFoundAll;

  @Autowired
  private InterfaceExchangeRateRepository repository;

  @Override
  protected InterfaceRepository<ExchangeRate, String> getRepository() {

    return repository;

  }

  @Override
  @CircuitBreaker(name = CIRCUIT, fallbackMethod = "findAllFallback")
  public Flux<ExchangeRate> findAll() {

    Flux<ExchangeRate> exchangeRateDatabase = repository.findAll()
        .switchIfEmpty(Mono.error(new RuntimeException(msgRateNotFoundAll)));

    return exchangeRateDatabase.flatMap(Flux::just);

  }

  @Override
  @CircuitBreaker(name = CIRCUIT, fallbackMethod = "createFallback")
  public Mono<ExchangeRate> create(ExchangeRate exchangeRate) {

    Flux<ExchangeRate> exchangeRateDatabase = repository.findAll();

    return exchangeRateDatabase
        .collectList()
        .flatMap(list -> {
          
          if (list.size() > 0) {
                        
            for (ExchangeRate rate : list) {
              
              rate.setDisabled(true);
              repository.save(rate);
                            
            }

          }
          
          exchangeRate.setDisabled(false);
          exchangeRate.setCreationDate(LocalDateTime.now());
          
          return repository.save(exchangeRate).map(createdObject -> createdObject);
          
        });

  }

  @Override
  @CircuitBreaker(name = CIRCUIT, fallbackMethod = "updateFallback")
  public Mono<ExchangeRate> updateRate(ExchangeRate exchangeRate, String id) {

    Mono<ExchangeRate> exchangeRateModification = Mono.just(exchangeRate);

    Mono<ExchangeRate> exchangeRateDatabase = findById(id);

    return exchangeRateDatabase
        .zipWith(exchangeRateModification, (a, b) -> {

          a.setDisabled(b.getDisabled());
          a.setBuyCoin(b.getBuyCoin());          
          return a;

        });

  }

  /** Mensaje si no existen monederos. */
  public Flux<List<ExchangeRate>> findAllFallback(Exception ex) {

    log.info("Tipo de Cambio no encontrados.");

    List<ExchangeRate> list = new ArrayList<>();

    list.add(ExchangeRate
        .builder()
        .id(ex.getMessage())
        .build());

    return Flux.just(list);

  }

  /** Mensaje si falla el create. */
  public Mono<ExchangeRate> createFallback(ExchangeRate exchangeRate, Exception ex) {

    log.info("Tipo de Cambio no se pudo crear.");

    return Mono.just(ExchangeRate
        .builder()
        .id(ex.getMessage())
        .build());

  }

  /** Mensaje si falla el update. */
  public Mono<ExchangeRate> updateFallback(ExchangeRate exchangeRate,
      String id, Exception ex) {

    log.info("Tipo de Cambio no encontrado para actualizar.");

    return Mono.just(ExchangeRate
        .builder()
        .id(ex.getMessage())
        .build());

  }

}

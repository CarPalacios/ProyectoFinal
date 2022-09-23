package com.everis.service.impl;

import com.everis.model.Request;
import com.everis.repository.InterfaceRepository;
import com.everis.repository.InterfaceRequestRepository;
import com.everis.service.InterfaceRequestService;

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
 * Implementacion de Metodos del Service Request.
 */
@Slf4j
@Service
public class RequestServiceImpl extends CrudServiceImpl<Request, String>
    implements InterfaceRequestService {

  static final String CIRCUIT = "walletServiceCircuitBreaker";

  @Value("${msg.error.registro.request.notfound.all}")
  private String msgRequestNotFoundAll;

  @Value("${msg.error.registro.notfound}")
  private String msgNotFound;

  @Value("${msg.error.registro.request.if.exists}")
  private String msgRequestIfExists;

  @Autowired
  private InterfaceRequestRepository repository;

  @Override
  protected InterfaceRepository<Request, String> getRepository() {

    return repository;

  }

  @Override
  @CircuitBreaker(name = CIRCUIT, fallbackMethod = "findAllFallback")
  public Flux<Request> findAll() {

    Flux<Request> requestDatabase = repository.findAll()
        .switchIfEmpty(Mono.error(new RuntimeException(msgRequestNotFoundAll)));

    return requestDatabase.flatMap(Flux::just);

  }

  @Override
  public Mono<Request> findByNumber(String number) {
    
    return repository.findByNumber(number)
      .switchIfEmpty(Mono.error(new RuntimeException(msgNotFound)));
    
  }

  @Override
  @CircuitBreaker(name = CIRCUIT, fallbackMethod = "createFallback")
  public Mono<Request> create(Request request) {

    Flux<Request> requestDatabase = repository.findAll()
        .filter(list -> list.getNumber().equalsIgnoreCase(request.getNumber()));
        
    return requestDatabase
        .collectList()
        .flatMap(list -> {
          
          if (list.size() > 0) {
            
            return Mono.error(new RuntimeException(msgRequestIfExists));

          }
          
          return repository.save(request).map(createdObject -> createdObject);
          
        });

  }

  /** Mensaje si no existen monederos. */
  public Flux<List<Request>> findAllFallback(Exception ex) {

    log.info("Solicitudes no encontrados.");

    List<Request> list = new ArrayList<>();

    list.add(Request
        .builder()
        .id(ex.getMessage())
        .build());

    return Flux.just(list);

  }

  /** Mensaje si falla el create. */
  public Mono<Request> createFallback(Request request, Exception ex) {

    log.info("Solicitud con numero {} no se pudo crear.", request.getNumber());

    return Mono.just(Request
        .builder()
        .number(request.getNumber())
        .id(ex.getMessage())
        .build());

  }

}

package com.everis.service.impl;

import com.everis.dto.Response;
import com.everis.model.Client;
import com.everis.repository.InterfaceClientRepository;
import com.everis.repository.InterfaceRepository;
import com.everis.service.InterfaceClientService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Implementacion de Metodos del Service Client.
 */
@Slf4j
@Service
public class ClientServiceImpl extends CrudServiceImpl<Client, String>
    implements InterfaceClientService {

  static final String CIRCUIT = "clientServiceCircuitBreaker";

  @Autowired
  private InterfaceClientRepository repository;

  @Autowired
  private InterfaceClientService service;

  @Override
  protected InterfaceRepository<Client, String> getRepository() {

    return repository;

  }

  @Override
  @CircuitBreaker(name = CIRCUIT, fallbackMethod = "findAllFallback")
  public Mono<List<Client>> findAllClient() {

    Flux<Client> clientDatabase = service.findAll()
        .switchIfEmpty(Mono.error(new Throwable("No hay clientes registrados")));

    return clientDatabase.collectList().flatMap(Mono::just);

  }

  @Override
  @CircuitBreaker(name = CIRCUIT, fallbackMethod = "clientFallback")
  public Mono<Client> findByIdentityNumber(String identityNumber) {

    return repository.findByIdentityNumber(identityNumber)
        .switchIfEmpty(Mono.error(new Throwable("DNI no encontrado")));

  }

  @Override
  @CircuitBreaker(name = CIRCUIT, fallbackMethod = "createFallback")
  public Mono<Client> createClient(Client client) {

    Flux<Client> clientDatabase = service.findAll()
        .filter(list -> list.getIdentityNumber().equals(client.getIdentityNumber()));

    return clientDatabase
        .collectList()
        .flatMap(list -> {

          if (list.size() > 0) {

            return Mono.error(new Throwable("El cliente ya existe"));

          }
          
          return repository.save(client);

        });

  }

  @Override
  @CircuitBreaker(name = CIRCUIT, fallbackMethod = "updateFallback")
  public Mono<Client> updateClient(Client client, String identityNumber) {

    Mono<Client> clientModification = Mono.just(client);

    Mono<Client> clientDatabase = repository.findByIdentityNumber(identityNumber);

    return clientDatabase
        .zipWith(clientModification, (a, b) -> {

          if (b.getName() != null) a.setName(b.getName());
          if (b.getAddress() != null) a.setAddress(b.getAddress());
          if (b.getPhoneNumber() != null) a.setPhoneNumber(b.getPhoneNumber());

          return a;
        });
  }

  @Override
  @CircuitBreaker(name = CIRCUIT, fallbackMethod = "deleteFallback")
  public Mono<Response> deleteClient(String identityNumber) {

    Mono<Client> clientDatabase = repository.findByIdentityNumber(identityNumber);

    return clientDatabase
        .flatMap(objectDelete -> service.delete(objectDelete.getId())
            .then(Mono.just(Response.builder().data("Cliente Eliminado").build())))
        .switchIfEmpty(Mono.error(new RuntimeException("Cliente no identificado para eliminar")));

  }

  /** Mensaje si no existen clientes. */
  public Mono<List<Client>> findAllFallback(Exception ex) {

    log.info("Clients not found, returning fallback");

    List<Client> list = new ArrayList<>();

    list.add(Client
        .builder()
        .name(ex.getMessage())
        .build());

    return Mono.just(list);

  }

  /** Mensaje si no encuentra el cliente. */
  public Mono<Client> clientFallback(String identityNumber, Exception ex) {

    log.info("Client with identity number {} not found.", identityNumber);

    return Mono.just(Client
        .builder()
        .identityNumber(identityNumber)
        .name(ex.getMessage())
        .build());

  }

  /** Mensaje si falla el create. */
  public Mono<Client> createFallback(Client client, Exception ex) {

    log.info("Client with identity number {} could not be created.", client.getIdentityNumber());

    return Mono.just(Client
        .builder()
        .identityNumber(client.getIdentityNumber())
        .name(ex.getMessage())
        .build());

  }

  /** Mensaje si falla el update. */
  public Mono<Client> updateFallback(Client client,
      String identityNumber, Exception ex) {

    log.info("Client with identity number {} not found to update.",
    		client.getIdentityNumber());

    return Mono.just(Client
        .builder()
        .identityNumber(identityNumber)
        .name(ex.getMessage())
        .build());

  }

  /** Mensaje si falla el delete. */
  public Mono<Response> deleteFallback(String identityNumber, Exception ex) {

    log.info("Client with identity number {} not found to delete.", identityNumber);

    return Mono.just(Response
        .builder()
        .data(identityNumber)
        .error(ex.getMessage())
        .build());

  }

}

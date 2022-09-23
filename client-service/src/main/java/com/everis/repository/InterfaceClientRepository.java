package com.everis.repository;

import com.everis.model.Client;
import reactor.core.publisher.Mono;

/**
 * Interface de Metodos del Repositorio.
 */
public interface InterfaceClientRepository extends InterfaceRepository<Client, String> {

  Mono<Client> findByIdentityNumber(String identityNumber);

}

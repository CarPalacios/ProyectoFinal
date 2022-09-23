package com.everis.service;

import com.everis.dto.Response;
import com.everis.model.Client;
import java.util.List;
import reactor.core.publisher.Mono;

/**
 * Interface de Metodos del Service Client.
 */
public interface InterfaceClientService extends InterfaceCrudService<Client, String> {

  Mono<List<Client>> findAllClient();

  Mono<Client> findByIdentityNumber(String identityNumber);

  Mono<Client> createClient(Client client);

  Mono<Client> updateClient(Client client, String identityNumber);

  Mono<Response> deleteClient(String identityNumber);

}

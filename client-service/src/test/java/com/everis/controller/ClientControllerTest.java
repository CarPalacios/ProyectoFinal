package com.everis.controller;

import com.everis.model.Client;
import com.everis.service.InterfaceClientService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ClientControllerTest {

  @MockBean
  InterfaceClientService service;

  @Autowired
  private WebTestClient client;

  @Test
  void testFindAll() {

    Client client1 = new Client();

    client1.setId("1");
    client1.setName("ALEJANDRO");
    client1.setIdentityType("DNI");
    client1.setIdentityNumber("87654321");
    client1.setCustomerType("PERSONAL");
    client1.setAddress("PERU");
    client1.setPhoneNumber("963852741");

    Mockito.when(service.findAllClient())
        .thenReturn(Flux.just(client1).collectList());

    client.get()
    .uri("/client")
    .accept(MediaType.APPLICATION_NDJSON)
    .exchange()
    .expectStatus().isOk();

  }

  @Test
  void testFindByIdentityNumber() {

    Client client1 = new Client();

    client1.setId("2");
    client1.setName("MANUEL");
    client1.setIdentityType("DNI");
    client1.setIdentityNumber("99663388");
    client1.setCustomerType("PERSONAL");
    client1.setAddress("PERU");
    client1.setPhoneNumber("963852741");

    Mockito.when(service.findByIdentityNumber(client1.getIdentityNumber()))
        .thenReturn(Mono.just(client1));

    client.get()
    .uri("/client/{identityNumber}", client1.getIdentityNumber())
    .accept(MediaType.APPLICATION_NDJSON)
    .exchange()
    .expectStatus().isOk();


  }

}
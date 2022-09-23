package com.everis.controller;

import com.everis.model.CreditConsumer;
import com.everis.service.InterfaceCreditConsumerService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * Controlador del Credit Consumer.
 */
@RestController
@RequestMapping("/credit")
public class CreditConsumerController {

  @Autowired
  private InterfaceCreditConsumerService service;
  
  /** Metodo para listar todos los consumos de credito. */
  @GetMapping
  public Mono<ResponseEntity<List<CreditConsumer>>> findAll() {
      
    return service.findAllCredit()
        .map(objectFound -> ResponseEntity
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(objectFound));
  
  }
  
  /** Metodo para crear un consumo de credito. */
  @PostMapping
  public Mono<ResponseEntity<CreditConsumer>> create(@RequestBody CreditConsumer creditConsumer) {
  
    return service.createCredit(creditConsumer)
        .map(objectFound -> ResponseEntity
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(objectFound));
    
  }
  
}

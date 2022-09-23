package com.everis.controller;

import com.everis.model.Deposit;
import com.everis.service.InterfaceDepositService;
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
 * Controlador del Deposit.
 */
@RestController
@RequestMapping("/deposit")
public class DepositController {

  @Autowired
  private InterfaceDepositService service;
  
  /** Metodo para listar todos los depositos. */
  @GetMapping
  public Mono<ResponseEntity<List<Deposit>>> findAll() {
    
    return service.findAllDeposit()
        .map(objectFound -> ResponseEntity
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(objectFound));
    
  }
  
  /** Metodo para crear un deposito. */
  @PostMapping
  public Mono<ResponseEntity<Deposit>> create(@RequestBody Deposit deposit) {
      
    return service.createDeposit(deposit)
        .map(objectFound -> ResponseEntity
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(objectFound));
  
  }
  
}

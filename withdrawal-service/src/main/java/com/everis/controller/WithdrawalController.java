package com.everis.controller;

import com.everis.model.Withdrawal;
import com.everis.service.InterfaceWithdrawalService;
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
 * Controlador del Withdrawal.
 */
@RestController
@RequestMapping("/withdrawal")
public class WithdrawalController {
  
  @Autowired
  private InterfaceWithdrawalService service;

  /** Metodo para listar todos los retiros. */  
  @GetMapping
  public Mono<ResponseEntity<List<Withdrawal>>> findAll() {
      
    return service.findAllWithdrawal()
        .map(objectFound -> ResponseEntity
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(objectFound));
  
  }
  
  /** Metodo para crear un retiro. */
  @PostMapping
  public Mono<ResponseEntity<Withdrawal>> create(@RequestBody Withdrawal withdrawal) {
  
    return service.createWithdrawal(withdrawal)
        .map(objectFound -> ResponseEntity
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(objectFound));
  
  }
  
}


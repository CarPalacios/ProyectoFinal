package com.everis.controller;

import com.everis.model.CreditPayment;
import com.everis.service.InterfaceCreditPaymentService;
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
 * Controlador del Credit Payment.
 */
@RestController
@RequestMapping("/payment")
public class CreditPaymentController {

  @Autowired
  private InterfaceCreditPaymentService service;

  /** Metodo para listar todos los pagos de credito. */
  @GetMapping
  public Mono<ResponseEntity<List<CreditPayment>>> findAll() {
  
    return service.findAllPayment()
        .map(objectFound -> ResponseEntity
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(objectFound));
    
  }
  
  /** Metodo para crear un pago de credito. */
  @PostMapping
  public Mono<ResponseEntity<CreditPayment>> create(@RequestBody CreditPayment creditPayment) {
    
    return service.createPayment(creditPayment)
        .map(objectFound -> ResponseEntity
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(objectFound));
    
  }
  
}

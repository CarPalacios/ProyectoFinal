package com.everis.controller;

import com.everis.dto.Response;
import com.everis.model.Product;
import com.everis.model.Purchase;
import com.everis.service.InterfacePurchaseService;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * Controlador del Purchase.
 */
@RestController
@RequestMapping("/purchase")
public class PurchaseController {

  @Autowired
  private InterfacePurchaseService service;

  /** Metodo para listar todos los purchase. */
  @GetMapping
  public Mono<ResponseEntity<List<Purchase>>> findAll() {

    return service.findAllPurchase()
        .map(objectFound -> ResponseEntity
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(objectFound));

  }

  /** Metodo para listar todos los purchase por numero de identidad. */
  @GetMapping("/{identityNumber}")
  public Mono<ResponseEntity<List<Purchase>>> findByIndentityNumber(@PathVariable("identityNumber")
          String identityNumber) {

    return service.findByIndentityNumber(identityNumber)
        .map(objectFound -> ResponseEntity
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(objectFound));

  }

  /** Metodo para listar todos los productos disponibles para el cliente. */
  @GetMapping("/available/{identityNumber}")
  public Mono<ResponseEntity<List<Product>>> findByAvailableProduct(@PathVariable("identityNumber")
          String identityNumber) {

    return service.findByAvailableProduct(identityNumber)
        .map(objectFound -> ResponseEntity
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(objectFound));

  }

  /** Metodo para crear un purchase. */
  @PostMapping
  public Mono<ResponseEntity<Purchase>> create(@Valid @RequestBody Purchase purchase) {

    return service.createPurchase(purchase)
        .map(objectFound -> ResponseEntity
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(objectFound));

  }

  /** Metodo para actualizar un purchase. */
  @PutMapping("/{id}")
  public Mono<ResponseEntity<Purchase>> update(@RequestBody Purchase purchase,
      @PathVariable("id") String id) {

    return service.updatePurchase(purchase, id)
        .map(objectUpdated -> ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(objectUpdated)
            );

  }

  /** Metodo para eliminar un purchase. */
  @DeleteMapping("/{cardName}")
  public Mono<ResponseEntity<Response>> delete(@PathVariable("cardName") String cardNumber) {

    return service.deletePurchase(cardNumber)
        .map(objectFound -> ResponseEntity
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(objectFound));

  }   

}

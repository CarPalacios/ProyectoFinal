package com.everis.controller;

import com.everis.dto.Response;
import com.everis.model.Transfer;
import com.everis.model.Wallet;
import com.everis.model.Withdrawal;
import com.everis.service.InterfaceTransferService;
import com.everis.service.InterfaceWalletService;
import java.util.List;
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
 * Controlador del Wallet.
 */
@RestController
@RequestMapping("/wallet")
public class WalletController {

  @Autowired
  private InterfaceWalletService service;

  @Autowired
  private InterfaceTransferService transferService;

  /** Metodo para listar todos los monederos. */
  @GetMapping
  public Mono<ResponseEntity<List<Wallet>>> findAll() {

    return service.findAllWallet()
        .map(objectFound -> ResponseEntity
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(objectFound));

  }

  /** Metodo para buscar monedero por numero de telefono. */
  @GetMapping("/{phoneNumber}")
  public Mono<ResponseEntity<Wallet>> findByPhoneNumber(@PathVariable("phoneNumber")
      String phoneNumber) {

    return service.findByPhoneNumber(phoneNumber)
        .map(objectFound -> ResponseEntity
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(objectFound));

  }

  /** Metodo para crear un monedero. */
  @PostMapping
  public Mono<ResponseEntity<Wallet>> create(@RequestBody Wallet wallet) {

    return service.createWallet(wallet)
        .map(objectFound -> ResponseEntity
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(objectFound));

  }

  /** Metodo para crear un monedero. */
  @PostMapping("/transfer")
  public Mono<ResponseEntity<Withdrawal>> createTransfer(@RequestBody Transfer transfer) {

    return transferService.createTransfer(transfer)
        .map(objectFound -> ResponseEntity
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(objectFound));

  }

  /** Metodo para actualizar monedero por numero de telefono. */
  @PutMapping("/{phoneNumber}")
  public Mono<ResponseEntity<Wallet>> update(@RequestBody
      Wallet wallet, @PathVariable("phoneNumber") String phoneNumber) {

    return service.updateWallet(wallet, phoneNumber)
        .map(objectFound -> ResponseEntity
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(objectFound));

  }

  /** Metodo para eliminar monedero por numero de telefono. */
  @DeleteMapping("/{phoneNumber}")
  public Mono<ResponseEntity<Response>> delete(@PathVariable("phoneNumber")
      String phoneNumber) {

    return service.deleteWallet(phoneNumber)
        .map(objectFound -> ResponseEntity
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(objectFound));

  }

}

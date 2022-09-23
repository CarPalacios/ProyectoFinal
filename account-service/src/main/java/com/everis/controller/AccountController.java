package com.everis.controller;

import com.everis.dto.Response;
import com.everis.model.Account;
import com.everis.service.InterfaceAccountService;
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
 * Controlador Account.
 */
@RestController
@RequestMapping("/account")
public class AccountController {

  @Autowired
  private InterfaceAccountService service;

  /** Metodo para listar cuentas. */
  @GetMapping
  public Mono<ResponseEntity<List<Account>>> findAll() {

    return service.findAllAccount()
        .map(objectFound -> ResponseEntity
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(objectFound));

  }

  /** Metodo para buscar cuenta por numero de cuenta. */
  @GetMapping("/{accountNumber}")
  public Mono<ResponseEntity<Account>> findByAccountNumber(@PathVariable("accountNumber")
      String accountNumber) {

    return service.findByAccountNumber(accountNumber)
        .map(objectFound -> ResponseEntity
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(objectFound));

  }

  /** Metodo para crear una cuenta. */
  @PostMapping
  public Mono<ResponseEntity<Account>> create(@RequestBody Account account) {

    return service.createAccount(account)
        .map(objectFound -> ResponseEntity
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(objectFound));

  }

  /** Metodo para actualizar una cuenta. */
  @PutMapping("/{id}")
  public Mono<ResponseEntity<Account>> update(@RequestBody Account account,
      @PathVariable("id") String id) {

    return service.updateAccount(account, id)
        .map(objectUpdated -> ResponseEntity
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(objectUpdated));

  }

  /** Metodo para eliminar una cuenta. */
  @DeleteMapping("/{accountNumber}")
  public Mono<ResponseEntity<Response>> delete(@PathVariable("accountNumber")
      String accountNumber) {

    return service.deleteAccount(accountNumber)
        .map(objectFound -> ResponseEntity
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(objectFound));

  }

}

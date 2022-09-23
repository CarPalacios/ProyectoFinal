package com.everis.service.impl;

import com.everis.dto.Response;
import com.everis.model.Account;
import com.everis.model.Purchase;
import com.everis.repository.InterfaceAccountRepository;
import com.everis.repository.InterfaceRepository;
import com.everis.service.InterfaceAccountService;
import com.everis.service.InterfacePurchaseService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Implementacion de Metodos del Service Account.
 */
@Slf4j
@Service
public class AccountServiceImpl extends CrudServiceImpl<Account, String>
    implements InterfaceAccountService {

  static final String CIRCUIT = "accountServiceCircuitBreaker";

  @Autowired
  private InterfaceAccountRepository repository;

  @Autowired
  private InterfaceAccountService service;

  @Autowired
  private InterfacePurchaseService purchaseService;

  @Override
  protected InterfaceRepository<Account, String> getRepository() {

    return repository;

  }

  @Override
  @CircuitBreaker(name = CIRCUIT, fallbackMethod = "findAllFallback")
  public Mono<List<Account>> findAllAccount() {

    Flux<Account> accountDatabase = service.findAll()
        .switchIfEmpty(Mono.error(new RuntimeException("CUENTAS NO IDENTIFICADAS")));

    return accountDatabase.collectList().flatMap(Mono::just);

  }

  @Override
  @CircuitBreaker(name = CIRCUIT, fallbackMethod = "accountFallback")
  public Mono<Account> findByAccountNumber(String accountNumber) {

    return repository.findByAccountNumber(accountNumber)
        .switchIfEmpty(Mono.error(new RuntimeException("CUENTA NO IDENTIFICADA")));

  }

  @Override
  @CircuitBreaker(name = CIRCUIT, fallbackMethod = "createFallback")
  public Mono<Account> createAccount(Account account) {

    Mono<Purchase> purchaseDatabase = purchaseService
        .findByCardNumber(account.getPurchase().getCardNumber())
        .switchIfEmpty(Mono.error(new RuntimeException("NUMERO DE TARJETA NO EXISTE")));

    Flux<Account> accountDatabaseCard = service.findAll()
        .filter(list -> list.getPurchase().getCardNumber()
        .equals(account.getPurchase().getCardNumber()));

    Flux<Account> accountDatabase = service.findAll()
        .filter(list -> list.getAccountNumber().equals(account.getAccountNumber()))
        .mergeWith(accountDatabaseCard);

    return purchaseDatabase
        .flatMap(purchase -> {

          account.setPurchase(purchase);
          account.setCurrentBalance(purchase.getAmountIni());
          account.setDateOpened(LocalDateTime.now());

          return accountDatabase
              .collectList()
              .flatMap(list -> list.size() > 0
                  ?
                      Mono.error(new RuntimeException("CUENTA YA EXISTENTE"))
                  :
                      service.create(account)
              );

        });

  }

  @Override
  @CircuitBreaker(name = CIRCUIT, fallbackMethod = "updateFallback")
  public Mono<Account> updateAccount(Account account, String accountNumber) {

    Mono<Account> accountModification = Mono.just(account);

    Mono<Account> accountDatabase = findByAccountNumber(accountNumber);

    return accountDatabase
        .zipWith(accountModification, (a, b) -> {

          a.setAccountNumber(b.getAccountNumber());
          return a;

        })
        .flatMap(service::update);

  }

  @Override
  @CircuitBreaker(name = CIRCUIT, fallbackMethod = "deleteFallback")
  public Mono<Response> deleteAccount(String accountNumber) {

    Mono<Account> accountDatabase = findByAccountNumber(accountNumber);

    return accountDatabase
        .flatMap(objectDelete -> {

          return service.delete(objectDelete.getId())
              .then(Mono.just(Response.builder().data("CUENTA ELIMINADA").build()));

        })
        .switchIfEmpty(Mono.error(new RuntimeException("CUENTA NO IDENTIFICADA PARA ELIMINAR")));

  }

  /** Mensaje si no existen account. */
  public Mono<List<Account>> findAllFallback(Exception ex) {

    log.info("Accounts not found.");

    List<Account> list = new ArrayList<>();

    list.add(Account
        .builder()
        .id(ex.getMessage())
        .build());

    return Mono.just(list);

  }

  /** Mensaje si no encuentra el account. */
  public Mono<Account> accountFallback(String accountNumber, Exception ex) {

    log.info("Account {} not found.", accountNumber);

    return Mono.just(Account
        .builder()
        .accountNumber(accountNumber)
        .id(ex.getMessage())
        .build());

  }

  /** Mensaje si falla el create. */
  public Mono<Account> createFallback(Account account, Exception ex) {

    log.info("Account {} could not be created.", account.getAccountNumber());

    return Mono.just(Account
        .builder()
        .accountNumber(account.getAccountNumber())
        .currentBalance(Double.parseDouble(account.getPurchase().getCardNumber()))
        .id(ex.getMessage())
        .build());

  }

  /** Mensaje si falla el update. */  
  public Mono<Account> updateFallback(Account account, String accountNumber, Exception ex) { 

    log.info("Account {} not found to update.", account.getAccountNumber());

    return Mono.just(Account
        .builder()
        .accountNumber(accountNumber)
        .id(ex.getMessage())
        .build());

  }

  /** Mensaje si falla el delete. */
  public Mono<Response> deleteFallback(String accountNumber, Exception ex) {

    log.info("Account {} not found to delete.", accountNumber);

    return Mono.just(Response
        .builder()
        .data(accountNumber)
        .error(ex.getMessage())
        .build());

  }

}

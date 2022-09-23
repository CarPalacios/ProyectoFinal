package com.everis.service;

import com.everis.dto.Response;
import com.everis.model.Account;
import java.util.List;
import reactor.core.publisher.Mono;

/**
 * Interface de Metodos del Account.
 */
public interface InterfaceAccountService extends InterfaceCrudService<Account, String> {

  Mono<List<Account>> findAllAccount();

  Mono<Account> findByAccountNumber(String accountNumber);

  Mono<Account> createAccount(Account account);

  Mono<Account> updateAccount(Account account, String id);

  Mono<Response> deleteAccount(String accountNumber);

}

package com.everis.service;

import com.everis.model.Wallet;
import reactor.core.publisher.Mono;

/**
 * Interface de Metodos del Service Wallet.
 */
public interface InterfaceWalletService extends InterfaceCrudService<Wallet, String> {

  Mono<Wallet> findByPhoneNumber(String phoneNumber);

}

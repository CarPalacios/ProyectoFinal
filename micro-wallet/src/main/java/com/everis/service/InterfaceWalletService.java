package com.everis.service;

import com.everis.dto.Response;
import com.everis.model.Wallet;
import java.util.List;
import reactor.core.publisher.Mono;

/**
 * Interface de Metodos del Service Wallet.
 */
public interface InterfaceWalletService extends InterfaceCrudService<Wallet, String> {

  Mono<List<Wallet>> findAllWallet();

  Mono<Wallet> findByPhoneNumber(String phoneNumber);

  Mono<Wallet> createWallet(Wallet wallet);

  Mono<Wallet> updateWallet(Wallet wallet, String identityNumber);

  Mono<Response> deleteWallet(String identityNumber);

}

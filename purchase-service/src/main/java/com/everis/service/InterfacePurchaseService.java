package com.everis.service;

import com.everis.dto.Response;
import com.everis.model.Product;
import com.everis.model.Purchase;
import java.util.List;
import reactor.core.publisher.Mono;

/**
 * Interface de Metodos del Servicio Purchase.
 */
public interface InterfacePurchaseService extends InterfaceCrudService<Purchase, String> {

  Mono<List<Purchase>> findAllPurchase();

  Mono<List<Purchase>> findByIndentityNumber(String identityNumber);

  Mono<List<Product>> findByAvailableProduct(String identityNumber);

  Mono<Purchase> findByCardNumber(String cardNumber);

  Mono<Purchase> createPurchase(Purchase purchase);

  Mono<Purchase> updatePurchase(Purchase purchase, String id);

  Mono<Response> deletePurchase(String productName);

}

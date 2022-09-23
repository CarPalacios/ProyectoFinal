package com.everis.service;

import com.everis.model.Product;
import reactor.core.publisher.Mono;

/**
 * Interface de Metodos del Product.
 */
public interface InterfaceProductService extends InterfaceCrudService<Product, String> {

  Mono<Product> findByProductName(String productName);

}

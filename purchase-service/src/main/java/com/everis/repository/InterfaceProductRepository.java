package com.everis.repository;

import com.everis.model.Product;
import reactor.core.publisher.Mono;

/**
 * Interface de Metodos del Repositorio.
 */
public interface InterfaceProductRepository extends InterfaceRepository<Product, String> {

  Mono<Product> findByProductName(String productName);

}

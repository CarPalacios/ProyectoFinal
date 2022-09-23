package com.everis.service;

import com.everis.dto.Response;
import com.everis.model.Product;
import java.util.List;
import reactor.core.publisher.Mono;

/**
 * Interface de Metodos del Service Product.
 */
public interface InterfaceProductService extends InterfaceCrudService<Product, String> {

  Mono<List<Product>> findAllProduct();

  Mono<Product> findByProductName(String productName);

  Mono<Product> createProduct(Product product);

  Mono<Product> updateProduct(Product product, String productName);

  Mono<Response> deleteProduct(String productName);

}

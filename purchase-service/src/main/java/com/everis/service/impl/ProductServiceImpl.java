package com.everis.service.impl;

import com.everis.model.Product;
import com.everis.repository.InterfaceProductRepository;
import com.everis.repository.InterfaceRepository;
import com.everis.service.InterfaceProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Implementacion de los Metodos del Producto.
 */
@Service
public class ProductServiceImpl extends CrudServiceImpl<Product, String>
        implements InterfaceProductService {

  @Autowired
  private InterfaceProductRepository repository;

  @Override
  protected InterfaceRepository<Product, String> getRepository() {

    return repository;

  }

  @Override
  public Mono<Product> findByProductName(String productName) {

    return repository.findByProductName(productName);

  }

}

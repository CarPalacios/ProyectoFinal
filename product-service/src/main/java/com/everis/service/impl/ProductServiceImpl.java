package com.everis.service.impl;

import com.everis.dto.Response;
import com.everis.model.Product;
import com.everis.repository.InterfaceProductRepository;
import com.everis.repository.InterfaceRepository;
import com.everis.service.InterfaceProductService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import java.util.ArrayList;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Implementacion de Metodos del Service Product.
 */
@Slf4j
@Service
public class ProductServiceImpl extends CrudServiceImpl<Product, String>
    implements InterfaceProductService {

  static final String CIRCUIT = "productServiceCircuitBreaker";

  @Autowired
  private InterfaceProductRepository repository;

  @Autowired
  private InterfaceProductService service;

  @Override
  protected InterfaceRepository<Product, String> getRepository() {

    return repository;

  }

  @Override
  @CircuitBreaker(name = CIRCUIT, fallbackMethod = "findAllFallback")
  public Mono<List<Product>> findAllProduct() {

    Flux<Product> productDatabase = service.findAll()
        .switchIfEmpty(Mono.error(new RuntimeException("PRODUCTOS NO ENCONTRADOS")));

    return productDatabase.collectList().flatMap(Mono::just);

  }

  @Override
  @CircuitBreaker(name = CIRCUIT, fallbackMethod = "productFallback")
  public Mono<Product> findByProductName(String productName) {

    return repository.findByProductName(productName)
        .switchIfEmpty(Mono.error(new RuntimeException("PRODUCTO NO IDENTIFICADO")));

  }

  @Override
  @CircuitBreaker(name = CIRCUIT, fallbackMethod = "createFallback")
  public Mono<Product> createProduct(Product product) {

    Flux<Product> productDatabase = service.findAll()
        .filter(list -> list.getProductName().equals(product.getProductName()));

    return productDatabase
        .collectList()
        .flatMap(list -> {

          if (list.size() > 0) {

            return Mono.error(new RuntimeException("PRODUCTO YA EXISTE"));

          }

          return service.create(product)
              .map(createdObject -> {

                return createdObject;

              })
              .switchIfEmpty(Mono.error(new RuntimeException("PRODUCTO NO SE PUDO CREAR")));

        });

  }

  @Override
  @CircuitBreaker(name = CIRCUIT, fallbackMethod = "updateFallback")
  public Mono<Product> updateProduct(Product product, String productName) {

    Mono<Product> productModification = Mono.just(product);

    Mono<Product> productDatabase = findByProductName(productName);

    return productDatabase
        .zipWith(productModification, (a, b) -> {

          a.getCondition().setHasMaintenanceFee(b.getCondition()
              .isHasMaintenanceFee());
          a.getCondition().setHasMonthlyTransactionLimit(b.getCondition()
                .isHasMonthlyTransactionLimit());
          a.getCondition().setHasDailyMonthlyTransactionLimit(b.getCondition()
                .isHasDailyMonthlyTransactionLimit());

          if (b.getCondition().getProductPerPersonLimit() != null) {
            a.getCondition().setProductPerPersonLimit(b.getCondition()
                .getProductPerPersonLimit());
          }

          if (b.getCondition().getProductPerBusinessLimit() != null) {
            a.getCondition().setProductPerBusinessLimit(b.getCondition()
                .getProductPerBusinessLimit());
          }

          return a;

        })
        .flatMap(service::update)
        .map(objectUpdated -> {

          return objectUpdated;

        })
        .switchIfEmpty(Mono.error(new RuntimeException("PRODUCTO NO IDENTIFICADO PARA ACTUALIZAR")));

  }

  @Override
  @CircuitBreaker(name = CIRCUIT, fallbackMethod = "deleteFallback")
  public Mono<Response> deleteProduct(String productName) {

    Mono<Product> productDatabase = findByProductName(productName);

    return productDatabase
        .flatMap(objectDelete -> service.delete(objectDelete.getId())
            .then(Mono.just(Response.builder().data("CLIENTE ELIMINADO").build())))
        .switchIfEmpty(Mono.error(new RuntimeException("CLIENTE NO IDENTIFICADO PARA ELIMINAR")));

  }

  /** Mensaje si no existen productos. */
  public Mono<List<Product>> findAllFallback(Exception ex) {

    log.info("Products not found.");

    List<Product> list = new ArrayList<>();

    list.add(Product
        .builder()
        .productName(ex.getMessage())
        .build());

    return Mono.just(list);

  }

  /** Mensaje si no encuentra el producto. */
  public Mono<Product> productFallback(String productName, Exception ex) {

    log.info("Products {} not found.", productName);

    return Mono.just(Product
        .builder()
        .productName(productName)
        .productType(ex.getMessage())
        .build());

  }

  /** Mensaje si falla el create. */
  public Mono<Product> createFallback(Product product, Exception ex) {

    log.info("Product {} could not be created.", product.getProductName());

    return Mono.just(Product
        .builder()
        .productName(product.getProductName())
        .productType(ex.getMessage())
        .build());

  }

  /** Mensaje si falla el update. */
  public Mono<Product> updateFallback(Product product, String productName, Exception ex) {

    log.info("Product {} not found to update.", product.getProductName());

    return Mono.just(Product
        .builder()
        .productName(productName)
        .productType(ex.getMessage())
        .build());

  }

  /** Mensaje si falla el delete. */
  public Mono<Response> deleteFallback(String productName, Exception ex) {

    log.info("Product {} not found to delete.", productName);

    return Mono.just(Response
        .builder()
        .data(productName)
        .error(ex.getMessage())
        .build());

  }

}

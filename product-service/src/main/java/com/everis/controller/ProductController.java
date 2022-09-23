package com.everis.controller;

import com.everis.dto.Response;
import com.everis.model.Product;
import com.everis.service.InterfaceProductService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * Controlador del Product.
 */
@RestController
@RequestMapping("/product")
public class ProductController {

  @Autowired
  private InterfaceProductService service;

  /** Metodo para listar todos los productos. */
  @GetMapping
  public Mono<ResponseEntity<List<Product>>> findAll() {

    return service.findAllProduct()
        .map(objectFound -> ResponseEntity
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(objectFound));

  }

  /** Metodo para buscar producto por nombre. */
  @GetMapping("/{productName}")
  public Mono<ResponseEntity<Product>> findByProductName(@PathVariable("productName")
      String productName) {

    return service.findByProductName(productName)
        .map(objectFound -> ResponseEntity
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(objectFound));

  }

  /** Metodo para crear producto. */
  @PostMapping
  public Mono<ResponseEntity<Product>> create(@RequestBody Product product) {

    return service.createProduct(product)
        .map(objectFound -> ResponseEntity
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(objectFound));

  }

  /** Metodo para actualizar producto. */
  @PutMapping("/{productName}")
  public Mono<ResponseEntity<Product>> update(@RequestBody
      Product product, @PathVariable("productName") String productName) {

    return service.updateProduct(product, productName)
        .map(objectFound -> ResponseEntity
                .ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(objectFound));

  }

  /** Metodo para eliminar producto por nombre. */
  @DeleteMapping("/{productName}")
  public Mono<ResponseEntity<Response>> delete(@PathVariable("productName") String productName) {

    return service.deleteProduct(productName)
        .map(objectFound -> ResponseEntity
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(objectFound));

  }

}

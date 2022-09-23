package com.everis.repository;

import com.everis.model.Purchase;
import java.util.List;
import org.springframework.data.mongodb.repository.Query;
import reactor.core.publisher.Mono;

/**
 * Interface de Metodos del Repositorio.
 */
public interface InterfacePurchaseRepository extends InterfaceRepository<Purchase, String> {

  @Query(value = "{'customerOwner.identityNumber': ? 0}")
  Mono<List<Purchase>> findByIdentityNumberAndProductId(String identityNumber, String idProduct);

  Mono<Purchase> findByCardNumber(String cardNumber);

}

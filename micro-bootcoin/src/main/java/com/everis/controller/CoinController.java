package com.everis.controller;

import com.everis.model.Coin;
import com.everis.model.ExchangeRate;
import com.everis.model.Request;
import com.everis.model.Transfer;
import com.everis.model.Withdrawal;
import com.everis.service.InterfaceCoinService;
import com.everis.service.InterfaceExchangeRateService;
import com.everis.service.InterfaceRequestService;
import com.everis.service.InterfaceTransferService;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * Controlador del Coin.
 */
@RestController
@RequestMapping("/coin")
public class CoinController {

  @Autowired
  private InterfaceCoinService service;
  
  @Autowired
  private InterfaceRequestService requestService;
  
  @Autowired
  private InterfaceExchangeRateService exchangeRateService;
  
  @Autowired
  private InterfaceTransferService transferService;
  
  /** Metodo para listar todos los monedero bootcoin. */
  @GetMapping
  public Mono<ResponseEntity<List<Coin>>> findAll() {

    return service.findAll()
        .collectList()
        .map(objectFound -> ResponseEntity
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(objectFound));

  }

  /** Metodo para listar todos las solicitudes bootcoin. */
  @GetMapping("/request")
  public Mono<ResponseEntity<List<Request>>> findAllRequest() {

    return requestService.findAll()
        .collectList()
        .map(objectFound -> ResponseEntity
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(objectFound));

  }

  /** Metodo para listar todos los tipos de cambio. */
  @GetMapping("/rate")
  public Mono<ResponseEntity<List<ExchangeRate>>> findAllExchangeRate() {

    return exchangeRateService.findAll()
        .collectList()
        .map(objectFound -> ResponseEntity
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(objectFound));

  }

  /** Metodo para crear un monedero bootcoin. */
  @PostMapping
  public Mono<ResponseEntity<Coin>> create(@RequestBody Coin coin) {

    return service.create(coin)
        .map(objectFound -> ResponseEntity
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(objectFound));

  }

  /** Metodo para crear una solicitud bootcoin. */
  @PostMapping("/request")
  public Mono<ResponseEntity<Request>> createRequest(@RequestBody Request request) {

    return requestService.create(request)
        .map(objectFound -> ResponseEntity
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(objectFound));

  }

  /** Metodo para crear una tasa de cambio. */
  @PostMapping("/rate")
  public Mono<ResponseEntity<ExchangeRate>> createExchangeRate(@RequestBody ExchangeRate exchangeRate) {

    return exchangeRateService.create(exchangeRate)
        .map(objectFound -> ResponseEntity
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(objectFound));

  }

  /** Metodo para crear un monedero. */
  @PostMapping("/transfer")
  public Mono<ResponseEntity<Withdrawal>> createTransfer(@RequestBody Transfer transfer) {

    Mono<Withdrawal> withdrawal = null;
    
    if (transfer.getPayMode().equalsIgnoreCase("YANKI")) {
      
      withdrawal = transferService.createTransferYanki(transfer);
      
    } else if (transfer.getPayMode().equalsIgnoreCase("TRANSFERENCIA")) {
      
      withdrawal = transferService.createTransferAccount(transfer);
      
    }
    
    return withdrawal
        .map(objectFound -> ResponseEntity
            .ok()
            .contentType(MediaType.APPLICATION_JSON)
            .body(objectFound));
    
  }

}

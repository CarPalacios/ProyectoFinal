package com.everis.service.impl;

import com.everis.model.Account;
import com.everis.model.Deposit;
import com.everis.model.ExchangeRate;
import com.everis.model.Request;
import com.everis.model.Transfer;
import com.everis.model.Wallet;
import com.everis.model.Withdrawal;
import com.everis.repository.InterfaceRepository;
import com.everis.repository.InterfaceTransferRepository;
import com.everis.service.InterfaceAccountService;
import com.everis.service.InterfaceCoinService;
import com.everis.service.InterfaceExchangeRateService;
import com.everis.service.InterfaceRequestService;
import com.everis.service.InterfaceTransferService;
import com.everis.service.InterfaceWalletService;
import com.everis.topic.producer.CoinProducer;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

/**
 * Implementacion de Metodos del Service Transfer.
 */
@Slf4j
@Service
public class TransferServiceImpl extends CrudServiceImpl<Transfer, String> 
    implements InterfaceTransferService {

  static final String CIRCUIT = "transferServiceCircuitBreaker";

  @Value("${msg.error.registro.phoneini.exists}")
  private String msgPhoneIniNotExists;

  @Value("${msg.error.registro.phonefin.exists}")
  private String msgPhoneFinNotExists;

  @Value("${msg.error.registro.accountini.exists}")
  private String msgAccountIniNotExists;

  @Value("${msg.error.registro.accountfin.exists}")
  private String msgAccountFinNotExists;

  @Value("${msg.error.registro.request.exists}")
  private String msgRequestNotExists;
  
  @Value("${msg.error.registro.positive}")
  private String msgPositive;
  
  @Value("${msg.error.registro.exceed}")
  private String msgExceed;
  
  @Autowired
  private InterfaceTransferRepository repository;

  @Autowired
  private InterfaceWalletService walletService;

  @Autowired
  private InterfaceAccountService accountService;

  @Autowired
  private InterfaceRequestService requestService;
  
  @Autowired
  private InterfaceCoinService service;

  @Autowired
  private InterfaceExchangeRateService exchangeRateService;

  @Autowired
  private CoinProducer producer;
  
  @Override
  protected InterfaceRepository<Transfer, String> getRepository() {
  
    return repository;
  
  }

  @Override
  @CircuitBreaker(name = CIRCUIT, fallbackMethod = "createFallback")
  public Mono<Withdrawal> createTransferYanki(Transfer transfer) {
    
    Mono<Wallet> send = walletService
        .findByPhoneNumber(transfer.getSendNumber())
        .switchIfEmpty(Mono.error(new RuntimeException(msgPhoneIniNotExists)));

    Mono<Wallet> receive = walletService
        .findByPhoneNumber(transfer.getReceiveNumber())
        .switchIfEmpty(Mono.error(new RuntimeException(msgPhoneFinNotExists)));
    
    Mono<List<ExchangeRate>> rate = exchangeRateService.findAll()
        .takeLast(1).collectList();

    Mono<Request> requestDatabase = requestService.findByNumber(transfer.getReceiveNumber())
        .switchIfEmpty(Mono.error(new RuntimeException(msgRequestNotExists)));
        
    Withdrawal withdrawal = Withdrawal.builder().build();
    
    Deposit deposit = Deposit.builder().build();
    
    return send
        .flatMap(s -> {
          
          return receive
              .flatMap(r -> {
                
                Mono<Account> sendAccount = accountService
                    .findByPurchaseCardNumber(s.getPurchase().getCardNumber())
                    .switchIfEmpty(Mono.error(new RuntimeException(msgAccountIniNotExists)));

                Mono<Account> receiveAccount = accountService
                    .findByPurchaseCardNumber(r.getPurchase().getCardNumber())
                    .switchIfEmpty(Mono.error(new RuntimeException(msgAccountFinNotExists)));
                                
                return rate
                    .flatMap(exr -> {
                      
                      Double sale = exr.get(0).getSaleCoin();
                      
                      return requestDatabase
                          .flatMap(req -> {
                           
                            Double amount = req.getAmount() * sale;
                      
                            return sendAccount
                              .flatMap(sendA -> {
                                
                                withdrawal.setAccount(sendA);
                                withdrawal.getAccount().setCurrentBalance(sendA
                                    .getCurrentBalance() - amount);
                                withdrawal.setPurchase(sendA.getPurchase());
                                withdrawal.setAmount(amount);
                                
                                if (withdrawal.getAccount().getCurrentBalance() < 0) {
                                  
                                  return Mono.error(new RuntimeException(msgExceed));
                                  
                                }
                                
                                return receiveAccount
                                    .flatMap(recieveA -> {
                                      
                                      deposit.setAccount(recieveA);
                                      deposit.getAccount().setCurrentBalance(recieveA
                                          .getCurrentBalance() + amount);
                                      deposit.setPurchase(recieveA.getPurchase());
                                      deposit.setAmount(amount);
                                      
                                      producer.sendCreatedTransferWithdrawalTopic(withdrawal);
                                      producer.sendCreatedTransferDepositTopic(deposit);

                                      requestService.delete(req.getId());
                                      
                                      return Mono.just(withdrawal);
                                      
                                    });
                                
                              });   
                      
                          });
                      
                    });
                
              });
          
        });
    
  }
  
  @Override
  @CircuitBreaker(name = CIRCUIT, fallbackMethod = "createFallback")
  public Mono<Withdrawal> createTransferAccount(Transfer transfer) {
    
    Mono<Account> send = accountService
        .findByAccountNumber(transfer.getSendNumber())
        .switchIfEmpty(Mono.error(new RuntimeException(msgAccountIniNotExists)));

    Mono<Account> receive = accountService
        .findByAccountNumber(transfer.getReceiveNumber())
        .switchIfEmpty(Mono.error(new RuntimeException(msgAccountFinNotExists)));
    
    Mono<List<ExchangeRate>> rate = exchangeRateService.findAll()
        .takeLast(1).collectList();

    Mono<Request> requestDatabase = requestService.findByNumber(transfer.getReceiveNumber())
        .switchIfEmpty(Mono.error(new RuntimeException(msgRequestNotExists)));
        
    Withdrawal withdrawal = Withdrawal.builder().build();
    
    Deposit deposit = Deposit.builder().build();
                                
    return rate
        .flatMap(exr -> {
          
          Double sale = exr.get(0).getSaleCoin();
          
          return requestDatabase
              .flatMap(req -> {
               
                Double amount = req.getAmount() * sale;
          
                return send
                  .flatMap(sendA -> {
                    
                    withdrawal.setAccount(sendA);
                    withdrawal.getAccount().setCurrentBalance(sendA
                        .getCurrentBalance() - amount);
                    withdrawal.setPurchase(sendA.getPurchase());
                    withdrawal.setAmount(amount);
                    
                    if (withdrawal.getAccount().getCurrentBalance() < 0) {
                      
                      return Mono.error(new RuntimeException(msgExceed));
                      
                    }
                    
                    return receive
                        .flatMap(recieveA -> {
                                                    
                          deposit.setAccount(recieveA);
                          deposit.getAccount().setCurrentBalance(recieveA
                              .getCurrentBalance() + amount);
                          deposit.setPurchase(recieveA.getPurchase());
                          deposit.setAmount(amount);
                          
                          producer.sendCreatedTransferWithdrawalTopic(withdrawal);
                          producer.sendCreatedTransferDepositTopic(deposit);
                          
                          requestService.delete(req.getId());
                          
                          return Mono.just(withdrawal);
                          
                        });
                    
                  });   
          
              });
          
        });
    
  }
  
  /** Mensaje si falla el transfer. */
  public Mono<Withdrawal> createFallback(Transfer transfer, Exception ex) {
  
    log.info("Transferencia del numero {} hacia el numero {} no se pudo realizar.",
        transfer.getSendNumber(), transfer.getReceiveNumber());
  
    return Mono.just(Withdrawal
        .builder()
        .id(ex.getMessage())
        .description(transfer.getSendNumber())
        .description2(transfer.getReceiveNumber())
        .build());
    
  }

}

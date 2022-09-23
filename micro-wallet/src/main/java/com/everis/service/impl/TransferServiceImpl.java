package com.everis.service.impl;

import com.everis.model.Account;
import com.everis.model.Deposit;
import com.everis.model.Transfer;
import com.everis.model.Wallet;
import com.everis.model.Withdrawal;
import com.everis.repository.InterfaceRepository;
import com.everis.repository.InterfaceTransferRepository;
import com.everis.service.InterfaceAccountService;
import com.everis.service.InterfaceTransferService;
import com.everis.service.InterfaceWalletService;
import com.everis.topic.producer.WalletProducer;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
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
  
  @Value("${msg.error.registro.positive}")
  private String msgPositive;
  
  @Value("${msg.error.registro.exceed}")
  private String msgExceed;
  
  @Autowired
  private InterfaceTransferRepository repository;

  @Autowired
  private InterfaceWalletService service;

  @Autowired
  private InterfaceAccountService accountService;

  @Autowired
  private WalletProducer producer;
  
  @Override
  protected InterfaceRepository<Transfer, String> getRepository() {
  
    return repository;
  
  }

  @Override
  @CircuitBreaker(name = CIRCUIT, fallbackMethod = "createFallback")
  public Mono<Withdrawal> createTransfer(Transfer transfer) {
    
    Mono<Wallet> sendWallet = service
        .findByPhoneNumber(transfer.getSendWallet().getPhoneNumber())
        .switchIfEmpty(Mono.error(new RuntimeException(msgPhoneIniNotExists)));

    Mono<Wallet> receiveWallet = service
        .findByPhoneNumber(transfer.getReceiveWallet().getPhoneNumber())
        .switchIfEmpty(Mono.error(new RuntimeException(msgPhoneFinNotExists)));
    
    Withdrawal withdrawal = Withdrawal.builder().build();
    
    Deposit deposit = Deposit.builder().build();
    
    return sendWallet
        .flatMap(send -> {
          
          if (transfer.getAmount() < 0) {
            
            return Mono.error(new RuntimeException(msgPositive));
            
          }
          
          return receiveWallet
              .flatMap(receive -> {
                
                Flux<Account> sendAccount = accountService.findAll()
                    .filter(a -> a.getPurchase().getCardNumber()
                        .equalsIgnoreCase(send.getPurchase().getCardNumber()))
                    .switchIfEmpty(Mono.error(new RuntimeException(msgAccountIniNotExists)));

                Flux<Account> receiveAccount = accountService.findAll()
                    .filter(a -> a.getPurchase().getCardNumber()
                        .equalsIgnoreCase(receive.getPurchase().getCardNumber()))
                    .switchIfEmpty(Mono.error(new RuntimeException(msgAccountFinNotExists)));
                        
                return sendAccount
                    .collectList()
                    .flatMap(sendA -> {
                      
                      withdrawal.setAccount(sendA.get(0));
                      withdrawal.getAccount().setCurrentBalance(sendA.get(0)
                          .getCurrentBalance() - transfer.getAmount());
                      withdrawal.setPurchase(sendA.get(0).getPurchase());
                      withdrawal.setAmount(transfer.getAmount());
                      
                      return receiveAccount
                          .collectList()
                          .flatMap(recieveA -> {
                            
                            deposit.setAccount(recieveA.get(0));
                            deposit.getAccount().setCurrentBalance(recieveA.get(0)
                                .getCurrentBalance() + transfer.getAmount());
                            deposit.setPurchase(recieveA.get(0).getPurchase());
                            deposit.setAmount(transfer.getAmount());
                            
                            if (withdrawal.getAccount().getCurrentBalance() < 0) {
                              
                              return Mono.error(new RuntimeException(msgExceed));
                              
                            }
                            
                            producer.sendCreatedTransferWithdrawalTopic(withdrawal);
                            producer.sendCreatedTransferDepositTopic(deposit);
                            
                            return Mono.just(withdrawal);
                                                      
                          });
                                          
                    });
                                 
              
              });
                  
        });
    
  }
  
  /** Mensaje si falla el transfer. */
  public Mono<Withdrawal> createFallback(Transfer transfer, Exception ex) {
  
    log.info("Transferencia del numero {} hacia el numero {} no se pudo realizar.",
        transfer.getSendWallet().getPhoneNumber(), transfer.getReceiveWallet().getPhoneNumber());
  
    return Mono.just(Withdrawal
        .builder()
        .id(ex.getMessage())
        .description(transfer.getSendWallet().getPhoneNumber())
        .description2(transfer.getReceiveWallet().getPhoneNumber())
        .build());
    
  }

}

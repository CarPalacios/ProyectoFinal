package com.everis.topic.producer;

import com.everis.model.Deposit;
import com.everis.model.Wallet;
import com.everis.model.Withdrawal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Clase Producer del Wallet.
 */
@Component
public class WalletProducer {

  @Autowired
  private KafkaTemplate<String, Object> kafkaTemplate;

  private String walletTopic = "created-wallet-topic";

  private String transferWithdrawalTopic = "created-wallet-withdrawal-topic";

  private String transferDepositTopic = "created-wallet-deposit-topic";

  /** Envia datos del wallet al topico. */
  public void sendCreatedWalletTopic(Wallet wallet) {

    kafkaTemplate.send(walletTopic, wallet);

  }

  /** Envia datos del transfer al topico. */
  public void sendCreatedTransferWithdrawalTopic(Withdrawal withdrawal) {
  
    kafkaTemplate.send(transferWithdrawalTopic, withdrawal);
  
  }

  /** Envia datos del transfer al topico. */
  public void sendCreatedTransferDepositTopic(Deposit deposit) {
  
    kafkaTemplate.send(transferDepositTopic, deposit);
  
  } 

}

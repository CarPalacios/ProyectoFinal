package com.everis.topic.producer;

import com.everis.model.Coin;
import com.everis.model.Deposit;
import com.everis.model.Withdrawal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Clase Producer del Coin.
 */
@Component
public class CoinProducer {

  @Autowired
  private KafkaTemplate<String, Object> kafkaTemplate;

  private String coinTopic = "created-coin-topic";

  private String transferWithdrawalTopic = "created-coin-withdrawal-topic";

  private String transferDepositTopic = "created-coin-deposit-topic";

  /** Envia datos del wallet al topico. */
  public void sendCreatedWalletTopic(Coin coin) {

    kafkaTemplate.send(coinTopic, coin);

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

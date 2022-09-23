package com.everis.topic.producer;

import com.everis.model.CreditConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Clase Productor del Credit Consumer.
 */
@Component
public class CreditConsumerProducer {
  
  @Autowired
  private KafkaTemplate<String, CreditConsumer> kafkaTemplate;

  private String creditConsumerTransactionTopic = "created-credit-consumer-topic";

  /** Envia datos del consumo de credito al topico. */
  public void sendCreditConsumerTransactionTopic(CreditConsumer creditConsumer) {
  
    kafkaTemplate.send(creditConsumerTransactionTopic, creditConsumer);
  
  }
  
}

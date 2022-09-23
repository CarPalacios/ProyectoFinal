package com.everis.topic.producer;

import com.everis.model.CreditPayment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Clase Productor del Credit Payment.
 */
@Component
public class CreditPaymentProducer {
  
  @Autowired
  private KafkaTemplate<String, CreditPayment> kafkaTemplate;

  private String creditPaymentTransactionTopic = "created-credit-payment-topic";

  /** Envia datos del pago de credito al topico. */
  public void sendCreditPaymentTransactionTopic(CreditPayment creditPayment) {
  
    kafkaTemplate.send(creditPaymentTransactionTopic, creditPayment);
  
  }
  
}

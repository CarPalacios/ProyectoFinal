package com.everis.topic.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;

import com.everis.model.Purchase;

public class PurchaseProducer {
	
	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;

	  private String createdPurchaseTopic = "created-purchase-topic";

	  /** Envia datos del purchase al topico. */
	  public void sendCreatePurchase(Purchase purchase) {

	    kafkaTemplate.send(createdPurchaseTopic, purchase);

	  }

}

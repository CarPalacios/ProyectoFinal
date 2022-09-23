package com.everis.topic.producer;

import com.everis.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class ProductProducer {

	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;

	private String createdProductTopic = "saved-product-topic";

	/** Envia datos del product al topico. */
	public void sendSavedProductTopic(Product product) {

		kafkaTemplate.send(createdProductTopic, product);

	}

}

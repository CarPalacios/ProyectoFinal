package com.everis.topic.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.everis.model.Client;

@Component
public class ClientProducer {

	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;

	private String createdCustomerTopic = "saved-customer-topic";

	/** Envia datos del customer al topico. */
	public void sendSavedCustomerTopic(Client client) {

		kafkaTemplate.send(createdCustomerTopic, client);

	}

}

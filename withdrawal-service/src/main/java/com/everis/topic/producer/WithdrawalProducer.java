package com.everis.topic.producer;

import com.everis.model.Withdrawal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class WithdrawalProducer {

	@Autowired
	private KafkaTemplate<String, Withdrawal> kafkaTemplate;

	private String withdrawalAccountTopic = "created-withdrawal-topic";

	/** Envia datos del retiro al topico. */
	public void sendWithdrawalAccountTopic(Withdrawal withdrawal) {

		kafkaTemplate.send(withdrawalAccountTopic, withdrawal);

	}

}

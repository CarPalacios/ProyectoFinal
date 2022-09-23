package com.everis.topic.consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.everis.model.Account;
import com.everis.model.Purchase;
import com.everis.model.Withdrawal;
import com.everis.service.InterfaceAccountService;
import com.everis.service.InterfacePurchaseService;
import com.everis.service.InterfaceWithdrawalService;
import com.everis.topic.producer.WithdrawalProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import reactor.core.Disposable;
import reactor.core.publisher.Mono;

@Component
public class WithdrawalConsumer {

	@Autowired
	private InterfaceAccountService accountService;

	@Autowired
	private InterfacePurchaseService purchaseService;

	@Autowired
	private InterfaceWithdrawalService withdrawalService;

	@Autowired
	private WithdrawalProducer withdrawalProducer;

	ObjectMapper objectMapper = new ObjectMapper();

	/** Consume del topico account. */
	@KafkaListener(topics = "created-account-topic", groupId = "withdrawal-group")
	public Disposable retrieveCreatedAccount(String data) throws JsonProcessingException {

		Account account = objectMapper.readValue(data, Account.class);

		return Mono.just(account).log().flatMap(accountService::update).subscribe();

	}

	/** Consume del topico purchase. */
	@KafkaListener(topics = "created-purchase-topic", groupId = "withdrawal-group")
	public Disposable retrieveCreatedPurchase(String data) throws JsonProcessingException {

		Purchase purchase = objectMapper.readValue(data, Purchase.class);

		if (purchase.getProduct().getProductType().equals("ACTIVO")) {

			return null;

		}

		return Mono.just(purchase).log().flatMap(purchaseService::update).subscribe();

	}

	/** Consume del topico transaction. */
	@KafkaListener(topics = "created-transfer-withdrawal-topic", groupId = "withdrawal-group")
	public Disposable retrieveCreatedWithdrawal(String data) throws JsonProcessingException {

		Withdrawal withdrawal = objectMapper.readValue(data, Withdrawal.class);

		withdrawalProducer.sendWithdrawalAccountTopic(withdrawal);

		return Mono.just(withdrawal).log().flatMap(withdrawalService::update).subscribe();

	}

}

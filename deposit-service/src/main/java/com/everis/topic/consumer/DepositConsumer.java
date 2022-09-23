package com.everis.topic.consumer;

import org.springframework.beans.factory.annotation.Autowired;

import com.everis.model.Account;
import com.everis.model.Deposit;
import com.everis.model.Purchase;
import com.everis.service.InterfaceAccountService;
import com.everis.service.InterfaceDepositService;
import com.everis.service.InterfacePurchaseService;
import com.everis.topic.producer.DepositProducer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

@Component
public class DepositConsumer {

	@Autowired
	private InterfaceAccountService accountService;

	@Autowired
	private InterfacePurchaseService purchaseService;

	@Autowired
	private InterfaceDepositService depositService;

	@Autowired
	private DepositProducer depositProducer;

	ObjectMapper objectMapper = new ObjectMapper();

	/** Consume del topico account. */
	@KafkaListener(topics = "created-account-topic", groupId = "deposit-group")
	public Disposable retrieveCreatedAccount(String data) throws JsonProcessingException {

		Account account = objectMapper.readValue(data, Account.class);

		return Mono.just(account).log().flatMap(accountService::update).subscribe();

	}

	/** Consume del topico purchase. */
	@KafkaListener(topics = "created-purchase-topic", groupId = "deposit-group")
	public Disposable retrieveCreatedPurchase(String data) throws JsonProcessingException {

		Purchase purchase = objectMapper.readValue(data, Purchase.class);

		if (purchase.getProduct().getProductType().equals("ACTIVO")) {

			return null;

		}

		return Mono.just(purchase).log().flatMap(purchaseService::update).subscribe();

	}

	/** Consume del topico transaction. */
	@KafkaListener(topics = "created-transfer-deposit-topic", groupId = "deposit-group")
	public Disposable retrieveCreatedDeposit(String data) throws JsonProcessingException {

		Deposit deposit = objectMapper.readValue(data, Deposit.class);

		depositProducer.sendDepositAccountTopic(deposit);

		return Mono.just(deposit).log().flatMap(depositService::update).subscribe();

	}

}

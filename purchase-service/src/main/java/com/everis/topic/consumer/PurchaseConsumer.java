package com.everis.topic.consumer;

import com.everis.model.Customer;
import com.everis.model.Product;
import com.everis.model.Purchase;
import com.everis.model.Transaction;
import com.everis.service.InterfaceCustomerService;
import com.everis.service.InterfaceProductService;
import com.everis.service.InterfacePurchaseService;
import com.everis.topic.producer.PurchaseProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

@Component
public class PurchaseConsumer {

	@Autowired
	private InterfaceProductService productService;

	@Autowired
	private InterfaceCustomerService customerService;

	@Autowired
	private InterfacePurchaseService purchaseService;

	@Autowired
	private PurchaseProducer producer;

	ObjectMapper objectMapper = new ObjectMapper();

	/** Consume del topico product. */
	@KafkaListener(topics = "saved-product-topic", groupId = "purchase-group")
	public Disposable retrieveSavedProduct(String data) throws JsonProcessingException {

		Product product = objectMapper.readValue(data, Product.class);

		return Mono.just(product).log().flatMap(productService::update).subscribe();

	}

	/** Consume del topico customer. */
	@KafkaListener(topics = "saved-customer-topic", groupId = "purchase-group")
	public Disposable retrieveSavedCustomer(String data) throws JsonProcessingException {

		Customer customer = objectMapper.readValue(data, Customer.class);

		return Mono.just(customer).log().flatMap(customerService::update).subscribe();

	}

	/** Consume del topico transaction. */
	@KafkaListener(topics = "created-transaction-topic", groupId = "purchase-group")
	public Disposable retrieveCreatedTransaction(String data) throws JsonProcessingException {

		Transaction transaction = objectMapper.readValue(data, Transaction.class);

		Mono<Purchase> monoPurchase = purchaseService.findById(transaction.getPurchase().getId());

		Mono<Transaction> monoTransaction = Mono.just(transaction);

		return monoPurchase.zipWith(monoTransaction, (a, b) -> {

			if (b.getTransactionType().equals("CONSUMO TARJETA CREDITO")) {

				a.setAmountFin(b.getPurchase().getAmountFin());
				b.getPurchase().setAmountFin(a.getAmountFin());

			} else if (b.getTransactionType().equals("PAGO TARJETA CREDITO")) {

				a.setAmountFin(a.getAmountIni());
				b.getPurchase().setAmountFin(a.getAmountFin());

			} else if (b.getTransactionType().equals("RETIRO")) {

				a.setAmountFin(a.getAmountFin() - b.getTransactionAmount());
				a.getProduct().getCondition().setMonthlyTransactionLimit(
						b.getPurchase().getProduct().getCondition().getMonthlyTransactionLimit());

			} else if (b.getTransactionType().equals("DEPOSITO")) {

				a.setAmountFin(a.getAmountFin() + b.getTransactionAmount());
				a.getProduct().getCondition().setMonthlyTransactionLimit(
						b.getPurchase().getProduct().getCondition().getMonthlyTransactionLimit());

			}

			producer.sendCreatePurchase(b.getPurchase());
			return a;

		}).flatMap(purchaseService::update).subscribe();

	}

}

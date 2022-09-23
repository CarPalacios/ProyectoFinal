package com.everis.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Clase Transaction.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "transaction")
@Data
public class Transaction {

  @Id
  private String id;

  @Field(name = "transactionType")
  private String transactionType;

  @Field(name = "transactionAmount")
  private Double transactionAmount;

  @Field(name = "purchase")
  private Purchase purchase;

  @Field(name = "description")
  private String description;

}

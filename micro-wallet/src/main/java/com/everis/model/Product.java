package com.everis.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Clase Product.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Product {

  @Field(name = "productName")
  private String productName;

  @Field(name = "productType")
  private String productType;

  @Field(name = "condition")
  private Condition condition;

}

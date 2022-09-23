package com.everis.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Clase Product.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "product")
@Data
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class Product {

  @Id
  private String id;

  @Field(name = "productName")
  private String productName;

  @Field(name = "productType")
  private String productType;

  @Field(name = "condition")
  private Condition condition;

}

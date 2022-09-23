package com.everis.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Clase Customer.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "customer")
@Data
public class Customer {

  @Id
  private String id;

  @Field(name = "name")
  private String name;

  @Field(name = "identityType")
  private String identityType;

  @Field(name = "identityNumber")
  private String identityNumber;

  @Field(name = "customerType")
  private String customerType;

  @Field(name = "address")
  private String address;

  @Field(name = "phoneNumber")
  private String phoneNumber;

}

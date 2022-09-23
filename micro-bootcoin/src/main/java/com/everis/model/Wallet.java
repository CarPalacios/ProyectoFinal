package com.everis.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Clase Wallet.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "wallet")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class Wallet {

  @Id
  private String id;

  @Field(name = "name")
  private String name;

  @Field(name = "identityType")
  private String identityType;

  @Field(name = "identityNumber")
  private String identityNumber;

  @Field(name = "phoneNumber")
  private String phoneNumber;

  @Field(name = "purchase")
  private Purchase purchase;
  
  @Field(name = "imei")
  private String imei;

  @Field(name = "email")
  private String email;
  
}

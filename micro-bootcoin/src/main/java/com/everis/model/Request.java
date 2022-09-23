package com.everis.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Clase Request.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "request")
@Data
@Builder(toBuilder = true)
public class Request {

  @Id
  private String id;

  @Field(name = "payMode")
  private String payMode;
  
  @Field(name = "number")
  private String number;
  
  @Field(name = "amount")
  private Double amount;

}

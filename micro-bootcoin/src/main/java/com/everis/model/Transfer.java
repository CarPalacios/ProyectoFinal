package com.everis.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Clase Transfer.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "transfer")
@Data
@Builder
public class Transfer {

  @Id
  private String id;

  @Field(name = "payMode")
  private String payMode;

  @Field(name = "sendNumber")
  private String sendNumber;

  @Field(name = "receiveNumber")
  private String receiveNumber;
  
}

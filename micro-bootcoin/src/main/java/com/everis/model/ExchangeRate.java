package com.everis.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Clase Request.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@Document(collection = "exchangerate")
@Data
@Builder(toBuilder = true)
public class ExchangeRate {

  @Id
  private String id;

  @Field(name = "saleCoin")
  private Double saleCoin;
  
  @Field(name = "buyCoin")
  private Double buyCoin;
  
  @Field(name = "disabled")
  private Boolean disabled;

  @Field(name = "creationDate")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime creationDate;

}

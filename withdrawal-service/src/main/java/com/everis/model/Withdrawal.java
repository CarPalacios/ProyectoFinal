package com.everis.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Clase Withdrawal.
 */
@Document(collection = "withdrawal")
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Withdrawal {

  @Id
  private String id;
  
  @Field(name = "account")
  private Account account;
  
  @Field(name = "amount")
  private Double amount;
  
  @Field(name = "Purchase")
  private Purchase purchase;

  @Field(name = "description")
  private String description;

  @Field(name = "withdrawalDate")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime withdrawalDate;

}
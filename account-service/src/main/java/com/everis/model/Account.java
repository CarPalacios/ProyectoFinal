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
 * Clase Account.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "account")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Account {

  @Id
  private String id;

  @Field(name = "accountNumber")
  private String accountNumber;

  @Field(name = "dateOpened")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime dateOpened;

  @Field(name = "dateClosed")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime dateClosed;

  @Field(name = "purchase")
  private Purchase purchase;

  @Field(name = "currentBalance")
  private double currentBalance;

}

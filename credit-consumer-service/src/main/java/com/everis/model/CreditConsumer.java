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
 * Clase Credit Consumer.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "credit")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreditConsumer {
  
  @Id
  private String id;
  
  @Field(name = "amount")
  private Double amount;
  
  @Field(name = "purchase")
  private Purchase purchase;
  
  @Field(name = "description")
  private String description;
  
  @Field(name = "consumDate")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime consumDate;

}

package com.everis.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Clase Purchase.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@Document(collection = "purchase")
@Data
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class Purchase {

  @Id
  private String id;

  @Field(name = "product")
  private Product product;

  @NotNull @NotEmpty
  @Field(name = "customerOwner")
  private List<Customer> customerOwner;

  @Field(name = "authorizedSigner")
  private List<Customer> authorizedSigner;

  @Field(name = "amountIni")
  private double amountIni;

  @Field(name = "amountFin")
  private double amountFin;

  @Field(name = "cardNumber")
  private String cardNumber;

  @Field(name = "purchaseDate")
  @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
  private LocalDateTime purchaseDate;

}

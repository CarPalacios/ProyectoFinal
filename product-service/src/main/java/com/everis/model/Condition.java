package com.everis.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

/**
 * Clase Condition.
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Condition {

  private boolean hasMaintenanceFee;
  private Double maintenanceFee;

  private boolean hasMonthlyTransactionLimit;
  private Integer monthlyTransactionLimit;

  private boolean hasDailyMonthlyTransactionLimit;
  private Integer dailyMonthlyTransactionLimit;

  private Integer productPerPersonLimit;
  private Integer productPerBusinessLimit;

}

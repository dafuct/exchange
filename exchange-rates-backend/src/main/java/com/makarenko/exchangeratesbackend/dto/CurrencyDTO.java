package com.makarenko.exchangeratesbackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CurrencyDTO {

  @JsonProperty("ccy")
  private String ccy;
  @JsonProperty("base_ccy")
  private String base_ccy;
  @JsonProperty("buy")
  private String buy;
  @JsonProperty("sale")
  private String sale;
}

package br.com.francis.currencyexchange.gateway.database.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class ExchangeRateProviderResponse {
    private Map<String, BigDecimal> rates;
}

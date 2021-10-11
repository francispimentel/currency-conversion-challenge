package br.com.francis.currencyexchange.domain.response;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Map;

@Data
public class ExchangeRateProviderResponse {
    private Map<String, BigDecimal> rates;
}

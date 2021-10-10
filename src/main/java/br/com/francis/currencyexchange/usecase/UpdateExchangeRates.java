package br.com.francis.currencyexchange.usecase;

import br.com.francis.currencyexchange.gateway.database.entity.EURConversionRate;
import br.com.francis.currencyexchange.gateway.database.entity.ExchangeRateProviderResponse;
import br.com.francis.currencyexchange.gateway.database.repository.EURConversionRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class UpdateExchangeRates {

    private final EURConversionRateRepository eurConversionRateRepository;

    @Value("${exchange-rate.provider.url}")
    private String EXCHANGE_RATE_PROVIDER_URL;

    @Value("${exchange-rate.provider.access-key}")
    private String EXCHANGE_RATE_PROVIDER_ACCESS_KEY;

    public void execute() {
        RestTemplate restTemplate = new RestTemplate();
        String baseURL = UriComponentsBuilder
                .fromHttpUrl(EXCHANGE_RATE_PROVIDER_URL)
                .buildAndExpand(EXCHANGE_RATE_PROVIDER_ACCESS_KEY)
                .toString();

        ResponseEntity<ExchangeRateProviderResponse> response = restTemplate.getForEntity(baseURL, ExchangeRateProviderResponse.class);
        ExchangeRateProviderResponse body = Objects.requireNonNull(response.getBody());

        for (Map.Entry<String, BigDecimal> rate : body.getRates().entrySet()) {
            EURConversionRate conversionRate = new EURConversionRate();
            conversionRate.setCurrency(rate.getKey());
            conversionRate.setExchangeRate(rate.getValue());
            conversionRate.setTimestamp(LocalDateTime.now());
            eurConversionRateRepository.save(conversionRate);
        }
    }
}

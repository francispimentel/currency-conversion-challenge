package br.com.francis.currencyexchange.usecase;

import br.com.francis.currencyexchange.domain.response.ExchangeRateProviderResponse;
import br.com.francis.currencyexchange.gateway.database.repository.EURConversionRateRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UpdateExchangeRatesTests {

    @InjectMocks
    private UpdateExchangeRates usecase;

    @Mock
    private EURConversionRateRepository eurConversionRateRepository;

    @Mock
    private RestTemplate restTemplate;

    private static final String PROVIDER_URL = "http://example.com/api?key={key}";
    private static final String PROVIDER_KEY = "QWERTY123";

    @Before
    public void setup() {
        ReflectionTestUtils.setField(usecase, "EXCHANGE_RATE_PROVIDER_URL", PROVIDER_URL);
        ReflectionTestUtils.setField(usecase, "EXCHANGE_RATE_PROVIDER_ACCESS_KEY", PROVIDER_KEY);
    }

    @Test
    public void shouldPersistRetrievedData() {
        var responseEntity = createResponseEntity();
        when(restTemplate.getForEntity(PROVIDER_URL.replace("{key}", PROVIDER_KEY), ExchangeRateProviderResponse.class))
                .thenReturn(responseEntity);
        usecase.execute();
        verify(eurConversionRateRepository, times(responseEntity.getBody().getRates().size())).save(any());
    }

    private ResponseEntity<ExchangeRateProviderResponse> createResponseEntity() {
        ExchangeRateProviderResponse response = new ExchangeRateProviderResponse();
        response.setRates(new HashMap<>());
        response.getRates().put("EUR", BigDecimal.ONE);
        response.getRates().put("BRL", BigDecimal.valueOf(0.16));
        response.getRates().put("USD", BigDecimal.valueOf(0.87));
        response.getRates().put("JPY", BigDecimal.valueOf(0.0076));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}

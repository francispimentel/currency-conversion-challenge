package br.com.francis.currencyexchange.usecase;

import br.com.francis.currencyexchange.domain.exception.BusinessException;
import br.com.francis.currencyexchange.gateway.database.entity.Conversion;
import br.com.francis.currencyexchange.gateway.database.entity.EURConversionRate;
import br.com.francis.currencyexchange.gateway.database.repository.EURConversionRateRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class GetExchangeRateTests {

    @InjectMocks
    private GetExchangeRate usecase;

    @Mock
    private UpdateExchangeRates updateExchangeRates;

    @Mock
    private EURConversionRateRepository eurConversionRateRepository;

    @Before
    public void setup() {
        ReflectionTestUtils.setField(usecase, "MINUTES_TO_EXPIRE_EXCHANGE_RATE", 30L);
    }

    @Test
    public void shouldThrowBusinessExceptionWhenCurrencyIsNotSupported() {
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            usecase.execute("XXX", "ZZZ");
        });
        assertEquals("API does not support conversions of currency code 'XXX'.", exception.getMessage());
    }

    @Test
    public void shouldGetTheExchangeRateAndReturnConversionObjectWithoutRequiringGlobalRefreshOfRates() {
        var rate1 = BigDecimal.valueOf(0.16);
        var rate2 = BigDecimal.valueOf(0.87);
        when(eurConversionRateRepository.findByCurrencyInOrderBySequenceDesc(anyList(), eq(PageRequest.of(0, 2))))
                .thenReturn(createValidExchangeRate("BRL", "USD", rate1, rate2));
        Conversion conversion = usecase.execute("BRL", "USD");
        assertEquals(BigDecimal.ONE.divide(rate1, 8, RoundingMode.HALF_EVEN).multiply(rate2), conversion.getExchangeRate());
        verify(updateExchangeRates, times(0)).execute();
    }

    @Test
    public void shouldReturnOneAsRateWhenCurrenciesAreTheSame() {
        Conversion conversion = usecase.execute("JPY", "JPY");
        assertEquals(BigDecimal.ONE, conversion.getExchangeRate());
    }

    @Test
    public void shouldRefreshExchangeRatesWhenNeededThenCalculateExchangeRate() {
        var rate1 = BigDecimal.valueOf(1);
        var rate2 = BigDecimal.valueOf(1);
        when(eurConversionRateRepository.findByCurrencyInOrderBySequenceDesc(anyList(), eq(PageRequest.of(0, 2))))
                .thenReturn(createValidExchangeRate("EUR", "EUR", rate1, rate1)) // First scenario -> no JPY data
                .thenReturn(createValidExchangeRate("EUR", "JPY", rate1, rate2)); // Second scenario -> JPY data is included after refresh
        Conversion conversion = usecase.execute("EUR", "JPY");
        assertEquals(BigDecimal.ONE.divide(rate1, 8, RoundingMode.HALF_EVEN).multiply(rate2), conversion.getExchangeRate());
        verify(updateExchangeRates, times(1)).execute();
    }

    private List<EURConversionRate> createValidExchangeRate(String currency1, String currency2, BigDecimal rate1, BigDecimal rate2) {
        List<EURConversionRate> rates = new ArrayList<EURConversionRate>();
        EURConversionRate r1 = new EURConversionRate();
        r1.setExchangeRate(rate1);
        r1.setCurrency(currency1);
        r1.setTimestamp(LocalDateTime.now());
        rates.add(r1);
        EURConversionRate r2 = new EURConversionRate();
        r2.setExchangeRate(rate2);
        r2.setCurrency(currency2);
        r2.setTimestamp(LocalDateTime.now());
        rates.add(r2);
        return rates;
    }
}

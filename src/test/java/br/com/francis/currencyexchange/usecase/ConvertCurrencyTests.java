package br.com.francis.currencyexchange.usecase;

import br.com.francis.currencyexchange.gateway.database.entity.Conversion;
import br.com.francis.currencyexchange.gateway.database.repository.ConversionRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ConvertCurrencyTests {

    @InjectMocks
    private ConvertCurrency usecase;

    @Mock
    private GetExchangeRate getExchangeRate;

    @Mock
    private ConversionRepository conversionRepository;

    private final Conversion USD_BRL_CONVERSION = createConversion(5.5);

    @Before
    public void setup() {
        when(getExchangeRate.execute("USD", "BRL")).thenReturn(USD_BRL_CONVERSION);
    }

    private Conversion createConversion(double exchangeRate) {
        Conversion conversion = new Conversion();
        conversion.setExchangeRate(BigDecimal.valueOf(exchangeRate));
        return conversion;
    }

    @Test
    public void shouldPerformTheConversion() {
        Long userID = 99L;
        BigDecimal value = BigDecimal.valueOf(35);
        Conversion result = usecase.execute(99L, "USD", "BRL", value);
        verify(conversionRepository).save(result);
        assertEquals(userID, result.getUserID());
        assertEquals(value.multiply(USD_BRL_CONVERSION.getExchangeRate()), result.getDestinationAmount());
    }
}

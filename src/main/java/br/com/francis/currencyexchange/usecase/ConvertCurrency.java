package br.com.francis.currencyexchange.usecase;

import br.com.francis.currencyexchange.gateway.database.entity.Conversion;
import br.com.francis.currencyexchange.gateway.database.repository.ConversionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class ConvertCurrency {

    private final GetExchangeRate getExchangeRate;
    private final ConversionRepository conversionRepository;

    /**
     * Performs the conversion based on obtained exchanged rate and persists transaction on database.
     *
     * @param userID
     * @param originCurrency
     * @param destinationCurrency
     * @param value
     * @return
     */
    public Conversion execute(Long userID, String originCurrency, String destinationCurrency, BigDecimal value) {
        Conversion conversion = getExchangeRate.execute(originCurrency, destinationCurrency);
        conversion.setOriginCurrency(originCurrency);
        conversion.setOriginAmount(value);
        conversion.setDestinationCurrency(destinationCurrency);
        conversion.setDestinationAmount(conversion.getExchangeRate().multiply(conversion.getOriginAmount()));
        conversion.setUserID(userID);
        conversionRepository.save(conversion);
        return conversion;
    }
}

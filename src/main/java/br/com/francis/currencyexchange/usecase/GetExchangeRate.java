package br.com.francis.currencyexchange.usecase;

import br.com.francis.currencyexchange.domain.exception.BusinessException;
import br.com.francis.currencyexchange.gateway.database.entity.Conversion;
import br.com.francis.currencyexchange.gateway.database.entity.EURConversionRate;
import br.com.francis.currencyexchange.gateway.database.repository.EURConversionRateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

import static java.lang.String.format;
import static java.util.Arrays.asList;

@Component
@RequiredArgsConstructor
public class GetExchangeRate {

    @Value("${exchange-rate.minutes-to-expire}")
    private Long MINUTES_TO_EXPIRE_EXCHANGE_RATE;

    private final UpdateExchangeRates updateExchangeRates;
    private final EURConversionRateRepository eurConversionRateRepository;

    /**
     * Calculates the exchange rate between two currencies, and then returns a Conversion object containing exchange rate and exchange timestamp.
     *
     * @param originCurrency
     * @param destinationCurrency
     * @return
     */
    public Conversion execute(String originCurrency, String destinationCurrency) {

        originCurrency = originCurrency.toUpperCase();
        destinationCurrency = destinationCurrency.toUpperCase();

        if (originCurrency.equals(destinationCurrency)) {
            return Conversion.builder().exchangeRate(BigDecimal.ONE).exchangeTimestamp(LocalDateTime.now()).build();
        }

        List<EURConversionRate> rates = eurConversionRateRepository
                .findByCurrencyInOrderBySequenceDesc(asList(originCurrency, destinationCurrency), PageRequest.of(0, 2));

        if (isExchangeRateUpdateNeeded(rates)) {
            updateExchangeRates.execute();
            rates = eurConversionRateRepository
                    .findByCurrencyInOrderBySequenceDesc(asList(originCurrency, destinationCurrency), PageRequest.of(0, 2));
        }

        BigDecimal originExchangeRate = getEurExchangeRate(originCurrency, rates);
        BigDecimal destinationExchangeRate = getEurExchangeRate(destinationCurrency, rates);

        return Conversion.builder()
                .exchangeRate(BigDecimal.ONE.divide(originExchangeRate, 8, RoundingMode.HALF_EVEN).multiply(destinationExchangeRate))
                .exchangeTimestamp(rates.stream().map(EURConversionRate::getTimestamp).min(LocalDateTime::compareTo).orElse(LocalDateTime.MIN)).build();
    }

    private BigDecimal getEurExchangeRate(String currency, List<EURConversionRate> rates) {
        return rates.stream()
                .filter(x -> x.getCurrency().equalsIgnoreCase(currency))
                .map(EURConversionRate::getExchangeRate).findAny()
                .orElseThrow(() -> new BusinessException(format("API does not support conversions of currency %s.", currency)));
    }

    /**
     * @return False if there are at least one conversion rate of each currency that is not older then configured threshold
     */
    private boolean isExchangeRateUpdateNeeded(List<EURConversionRate> rates) {
        return rates.size() < 2
                || (rates.get(0).getCurrency().equalsIgnoreCase(rates.get(1).getCurrency()))
                || (rates.stream().map(EURConversionRate::getTimestamp).min(LocalDateTime::compareTo).orElse(LocalDateTime.MIN)
                .isBefore(LocalDateTime.now().minusMinutes(MINUTES_TO_EXPIRE_EXCHANGE_RATE)));
    }
}

package br.com.francis.currencyexchange.controller;

import br.com.francis.currencyexchange.domain.response.CurrencyConversionResponse;
import br.com.francis.currencyexchange.usecase.ConvertCurrency;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequiredArgsConstructor
@RequestMapping("convert")
public class CurrencyConversionWS {

    private final ConvertCurrency convertCurrency;

    @ApiOperation(value = "Converts monetary value from one currency to another.")
    @GetMapping
    public CurrencyConversionResponse convertCurrency(@RequestParam Long userID, @RequestParam String originCurrency, @RequestParam String destinationCurrency,
                                                      @RequestParam BigDecimal value) {
        return new CurrencyConversionResponse(convertCurrency.execute(userID, originCurrency, destinationCurrency, value));
    }
}

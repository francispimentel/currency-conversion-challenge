package br.com.francis.currencyexchange.domain.response;

import br.com.francis.currencyexchange.gateway.database.entity.Conversion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class CurrencyConversionResponse extends DefaultResponse {

    private Conversion conversion;
}

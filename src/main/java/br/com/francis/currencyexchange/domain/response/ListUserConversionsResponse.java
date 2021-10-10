package br.com.francis.currencyexchange.domain.response;

import br.com.francis.currencyexchange.gateway.database.entity.Conversion;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListUserConversionsResponse extends DefaultResponse {
    private List<Conversion> conversions;
}

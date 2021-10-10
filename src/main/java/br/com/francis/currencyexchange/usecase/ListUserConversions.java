package br.com.francis.currencyexchange.usecase;

import br.com.francis.currencyexchange.gateway.database.entity.Conversion;
import br.com.francis.currencyexchange.gateway.database.repository.ConversionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ListUserConversions {

    private final ConversionRepository conversionRepository;

    public List<Conversion> execute(Long userID) {
        return conversionRepository.findByUserIDOrderByTransactionIDDesc(userID);
    }
}

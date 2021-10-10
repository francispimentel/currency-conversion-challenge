package br.com.francis.currencyexchange.controller;

import br.com.francis.currencyexchange.domain.response.ListUserConversionsResponse;
import br.com.francis.currencyexchange.gateway.database.entity.Conversion;
import br.com.francis.currencyexchange.usecase.ListUserConversions;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("user")
public class UserWS {

    private final ListUserConversions listUserConversions;

    @GetMapping("/{userID}/conversion/all")
    public ListUserConversionsResponse listUserConversions(@PathVariable Long userID) {
        List<Conversion> conversionList = listUserConversions.execute(userID);
        return new ListUserConversionsResponse(conversionList);
    }
}

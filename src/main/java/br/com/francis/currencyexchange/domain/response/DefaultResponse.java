package br.com.francis.currencyexchange.domain.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DefaultResponse {

    private Integer httpStatus = 200;
    private String message = "OK";
}

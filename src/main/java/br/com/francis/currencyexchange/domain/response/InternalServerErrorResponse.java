package br.com.francis.currencyexchange.domain.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InternalServerErrorResponse extends DefaultResponse {

    private LocalDateTime timestamp;

    public InternalServerErrorResponse() {
        super(500, "Internal server error. Please send this payload to system administrator");
        this.timestamp = LocalDateTime.now();
    }
}

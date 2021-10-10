package br.com.francis.currencyexchange.controller.exception;

import br.com.francis.currencyexchange.domain.exception.BusinessException;
import br.com.francis.currencyexchange.domain.response.DefaultResponse;
import br.com.francis.currencyexchange.domain.response.InternalServerErrorResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static java.lang.String.format;

@ControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class ControllerExceptionAdvisor {

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    protected ResponseEntity<DefaultResponse> missingParameters(MissingServletRequestParameterException ex) {
        DefaultResponse response = new DefaultResponse(HttpStatus.BAD_REQUEST.value(), format("Please provide '%s' as URL parameter.", ex.getParameterName()));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = BusinessException.class)
    protected ResponseEntity<DefaultResponse> businessException(BusinessException ex) {
        DefaultResponse response = new DefaultResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = Exception.class)
    protected ResponseEntity<DefaultResponse> internalServerError(Exception ex) {
        log.error("", ex);
        return new ResponseEntity<>(new InternalServerErrorResponse(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}


package br.com.francis.currencyexchange.gateway.database.entity;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "EUR_CONVERSION_RATE")
public class EURConversionRate {

    @Id
    @SequenceGenerator(name = "SEQ_EUR_CONVERSION_RATE", sequenceName = "SEQ_EUR_CONVERSION_RATE")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_EUR_CONVERSION_RATE")
    @Column(name = "SEQUENCE")
    private Long sequence;

    @Column(name = "CURRENCY")
    private String currency;

    @Column(name = "EXCHANGE_RATE", nullable = false, columnDefinition = "DECIMAL(30, 8)")
    public BigDecimal exchangeRate;

    @Column(name = "TIMESTAMP", nullable = false)
    public LocalDateTime timestamp;
}

package br.com.francis.currencyexchange.gateway.database.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "CONVERSION")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Conversion {

    @Id
    @SequenceGenerator(name = "SEQ_CONVERSION", sequenceName = "SEQ_CONVERSION")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CONVERSION")
    @Column(name = "TRANSACTION_ID", nullable = false)
    private Long transactionID;

    @Column(name = "USER_ID", nullable = false)
    private Long userID;

    @Column(name = "ORIGIN_CURRENCY", nullable = false)
    private String originCurrency;

    @Column(name = "ORIGIN_AMOUNT", nullable = false, columnDefinition = "DECIMAL(30, 8)")
    private BigDecimal originAmount;

    @Column(name = "DEST_CURRENCY", nullable = false)
    private String destinationCurrency;

    @Column(name = "DEST_AMOUNT", nullable = false, columnDefinition = "DECIMAL(30, 8)")
    private BigDecimal destinationAmount;

    @Column(name = "EXCHANGE_RATE", nullable = false, columnDefinition = "DECIMAL(30, 8)")
    private BigDecimal exchangeRate;

    @Column(name = "EXCHANGE_TIMESTAMP", nullable = false)
    private LocalDateTime exchangeTimestamp;
}

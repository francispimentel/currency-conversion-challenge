package br.com.francis.currencyexchange.gateway.database.repository;

import br.com.francis.currencyexchange.gateway.database.entity.EURConversionRate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EURConversionRateRepository extends JpaRepository<EURConversionRate, Long> {

    List<EURConversionRate> findByCurrencyInOrderBySequenceDesc(List<String> currencies, Pageable pageable);
}

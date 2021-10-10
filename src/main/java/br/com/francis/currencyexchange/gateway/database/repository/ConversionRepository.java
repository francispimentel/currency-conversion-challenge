package br.com.francis.currencyexchange.gateway.database.repository;

import br.com.francis.currencyexchange.gateway.database.entity.Conversion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConversionRepository extends JpaRepository<Conversion, Long> {

    List<Conversion> findByUserIDOrderByTransactionIDDesc(Long userID);
}

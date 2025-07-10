package com.moola.fx.moneychanger.operations.repository;

import com.moola.fx.moneychanger.operations.model.ComputeRate;
import com.moola.fx.moneychanger.operations.model.ComputeRateId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ComputeRateRepository extends JpaRepository<ComputeRate, ComputeRateId> {
    Optional<ComputeRate> findByIdCurrencyCodeAndIdMoneyChangerId(String currencyCode, Long moneyChangerId);
    List<ComputeRate> findByIdMoneyChangerId(Long moneyChangerId);
    boolean existsByIdCurrencyCodeAndIdMoneyChangerId(String currencyCode, Long moneyChangerId);
    @Modifying
    @Transactional
    @Query(value = """
            DELETE cr FROM compute_rates cr
            JOIN currency_code c ON c.currency = cr.currency_code
            LEFT JOIN money_changer_currency mcc
                 ON mcc.money_changer_id = cr.money_changer_id
                 AND mcc.currency_id = c.id
                 AND mcc.is_deleted = false
            WHERE mcc.id IS NULL AND cr.money_changer_id = :moneyChangerId
            """, nativeQuery = true)
    int deleteOrphanedComputeRates(@Param("moneyChangerId") Long moneyChangerId);
}

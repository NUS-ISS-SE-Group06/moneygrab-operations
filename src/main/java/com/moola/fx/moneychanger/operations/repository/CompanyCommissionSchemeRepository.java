package com.moola.fx.moneychanger.operations.repository;

import com.moola.fx.moneychanger.operations.model.CompanyCommissionScheme;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyCommissionSchemeRepository extends JpaRepository<CompanyCommissionScheme, Integer> {
    boolean existsByMoneyChangerId_IdAndCommissionRateId_Id(Long moneyChangerId, Integer commissionRateId);
}
package com.moneychanger_api.repository;

import com.moneychanger_api.model.CompanyCommissionScheme;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyCommissionSchemeRepository extends JpaRepository<CompanyCommissionScheme, Integer> {
    boolean existsByMoneyChangerId_IdAndCommissionRateId_Id(Long moneyChangerId, Integer commissionRateId);
}
package com.moola.fx.moneychanger.operations.repository;

import com.moola.fx.moneychanger.operations.model.CompanyCommissionScheme;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CompanyCommissionSchemeRepository extends JpaRepository<CompanyCommissionScheme, Integer> {
    boolean existsByMoneyChangerId_idAndSchemeId_IdAndIsDeletedFalse(Long moneyChangerId, Integer schemeId);
    boolean existsByMoneyChangerId_IdAndSchemeId_IdAndIdNotAndIsDeletedFalse(Long moneyChangerId, Integer schemeId, Integer id);
    List<CompanyCommissionScheme>   findBySchemeId_idAndIsDeletedFalse(Integer schemeId);
    Optional<CompanyCommissionScheme> findByIdAndIsDeletedFalse(Integer id);
    boolean existsBySchemeId_IdAndIsDeletedFalse(Integer schemeId);
}
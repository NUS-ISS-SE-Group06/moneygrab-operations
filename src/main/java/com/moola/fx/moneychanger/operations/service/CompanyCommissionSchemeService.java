package com.moola.fx.moneychanger.operations.service;

import com.moola.fx.moneychanger.operations.dto.CompanyCommissionSchemeDTO;
import com.moola.fx.moneychanger.operations.model.CompanyCommissionScheme;

import java.util.List;

public interface CompanyCommissionSchemeService {
    List<CompanyCommissionScheme> listAll();
    CompanyCommissionScheme get(Integer id);
    CompanyCommissionScheme save(CompanyCommissionScheme entity);
    CompanyCommissionScheme save(CompanyCommissionSchemeDTO dto);
    void delete(Integer id, Integer userId);
    List<CompanyCommissionScheme> findBySchemeId(Integer id);
}
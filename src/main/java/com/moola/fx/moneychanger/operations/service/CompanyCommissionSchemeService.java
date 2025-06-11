package com.moola.fx.moneychanger.operations.service;

import com.moola.fx.moneychanger.operations.model.CompanyCommissionScheme;

import java.util.List;

public interface CompanyCommissionSchemeService {
    List<CompanyCommissionScheme> listAll();
    CompanyCommissionScheme get(Integer id);
    CompanyCommissionScheme save(CompanyCommissionScheme item);
    void delete(Integer id);
}
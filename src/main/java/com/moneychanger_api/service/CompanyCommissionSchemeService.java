package com.moneychanger_api.service;

import com.moneychanger_api.model.CompanyCommissionScheme;

import java.util.List;

public interface CompanyCommissionSchemeService {
    List<CompanyCommissionScheme> listAll();
    CompanyCommissionScheme get(Integer id);
    CompanyCommissionScheme save(CompanyCommissionScheme item);
    void delete(Integer id);
}
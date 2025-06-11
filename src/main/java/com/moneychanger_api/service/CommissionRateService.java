package com.moneychanger_api.service;

import com.moneychanger_api.model.CommissionRate;

import java.util.List;

public interface CommissionRateService {
    List<CommissionRate> listAll();
    CommissionRate get(Integer id);
    CommissionRate save(CommissionRate item);
    void delete(Integer id, Integer userId);
    List<CommissionRate> findBySchemeId(Integer schemeId);
}
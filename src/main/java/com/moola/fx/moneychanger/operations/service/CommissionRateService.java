package com.moola.fx.moneychanger.operations.service;

import com.moola.fx.moneychanger.operations.model.CommissionRate;

import java.util.List;

public interface CommissionRateService {
    List<CommissionRate> listAll();
    CommissionRate get(Integer id);
    CommissionRate save(CommissionRate entity);
    void delete(Integer id, Integer userId);
    List<CommissionRate> findBySchemeId(Integer id);
}
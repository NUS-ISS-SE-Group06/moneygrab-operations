package com.moola.fx.moneychanger.operations.service;

import com.moola.fx.moneychanger.operations.model.CommissionRate;

import java.util.List;

public interface CommissionRateService {
    List<CommissionRate> listAll();
    CommissionRate get(Integer id);
    CommissionRate save(CommissionRate item);
    void delete(Integer id);
}
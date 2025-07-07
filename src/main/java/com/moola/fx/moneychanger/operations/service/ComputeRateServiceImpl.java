package com.moola.fx.moneychanger.operations.service;

import com.moola.fx.moneychanger.operations.exception.ResourceNotFoundException;
import com.moola.fx.moneychanger.operations.model.ComputeRate;
import com.moola.fx.moneychanger.operations.repository.ComputeRateRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ComputeRateServiceImpl implements ComputeRateService {

    private final ComputeRateRepository repo;

    @Autowired
    public ComputeRateServiceImpl(ComputeRateRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<ComputeRate> listAll() {
        return repo.findAll();
    }

    @Override
    public ComputeRate get(String currencyCode, Long moneyChangerId) {
        return repo.findByIdCurrencyCodeAndIdMoneyChangerId(currencyCode, moneyChangerId)
                .orElseThrow(() -> new ResourceNotFoundException("ComputeRate not found for currencyCode " + currencyCode + " and moneyChangerId " + moneyChangerId));
    }

    @Override
    @Transactional
    public List<ComputeRate> saveAll(List<ComputeRate> entities) {
        if (entities.isEmpty()) return List.of();
        return repo.saveAll(entities);
    }


    @Override
    public void delete(String currencyCode, Long moneyChangerId) {
        ComputeRate existing = repo.findByIdCurrencyCodeAndIdMoneyChangerId(currencyCode, moneyChangerId)
                .orElseThrow(() -> new ResourceNotFoundException("ComputeRate not found for currencyCode " + currencyCode + " and moneyChangerId " + moneyChangerId));

        repo.delete(existing);
    }

    @Override
    public List<ComputeRate> findByMoneyChangerId(Long moneyChangerId) {
        return repo.findByIdMoneyChangerId(moneyChangerId);
    }

}

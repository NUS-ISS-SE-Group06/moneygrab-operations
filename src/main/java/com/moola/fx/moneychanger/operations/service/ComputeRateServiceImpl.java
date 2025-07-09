package com.moola.fx.moneychanger.operations.service;

import com.moola.fx.moneychanger.operations.exception.ForeignKeyConstraintException;
import com.moola.fx.moneychanger.operations.exception.ResourceNotFoundException;
import com.moola.fx.moneychanger.operations.model.ComputeRate;
import com.moola.fx.moneychanger.operations.repository.ComputeRateRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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

        try {
            return repo.saveAll(entities);
        } catch (DataIntegrityViolationException ex) {
            String detailedMessage = ex.getMostSpecificCause() != null
                    ? ex.getMostSpecificCause().getMessage()
                    : "Unknown data integrity violation";
            throw new ForeignKeyConstraintException("Foreign key constraint violated while saving ComputeRates: " + detailedMessage);
        }
    }


    @Override
    public void delete(String currencyCode, Long moneyChangerId) {
        ComputeRate existing = repo.findByIdCurrencyCodeAndIdMoneyChangerId(currencyCode, moneyChangerId)
                .orElseThrow(() -> new ResourceNotFoundException("ComputeRate not found for currencyCode " + currencyCode + " and moneyChangerId " + moneyChangerId));

        repo.delete(existing);
    }

    @Override
    @Transactional
    public void deleteOrphanedRates(Long moneyChangerId) {
        repo.deleteOrphanedComputeRates(moneyChangerId);
    }


    @Override
    public List<ComputeRate> findByMoneyChangerId(Long moneyChangerId) {
        return repo.findByIdMoneyChangerId(moneyChangerId);
    }

}

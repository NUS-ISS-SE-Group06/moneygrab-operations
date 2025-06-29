package com.moola.fx.moneychanger.operations.service;

import com.moola.fx.moneychanger.operations.exception.DuplicateResourceException;
import com.moola.fx.moneychanger.operations.exception.ResourceNotFoundException;
import com.moola.fx.moneychanger.operations.model.MoneyChangerCurrency;
import com.moola.fx.moneychanger.operations.repository.MoneyChangerCurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MoneyChangerCurrencyServiceImpl implements MoneyChangerCurrencyService {

    private final MoneyChangerCurrencyRepository repo;

    @Autowired
    public MoneyChangerCurrencyServiceImpl(MoneyChangerCurrencyRepository repo ) {
        this.repo = repo;
    }

    @Override
    public List<MoneyChangerCurrency> listAll() {
        return repo.findByIsDeletedFalse();
    }

    @Override
    public MoneyChangerCurrency get(Integer id) {
        return repo.findByIdAndIsDeletedFalse(id).orElseThrow(() -> new ResourceNotFoundException("Money Changer Currency with ID " + id + " not found  or has been deleted"));
    }


    @Override
    @Transactional
    public MoneyChangerCurrency save(MoneyChangerCurrency entity) {
        boolean exists=(entity.getId() == null)
                ? repo.existsByMoneyChangerIdAndCurrencyIdAndIsDeletedFalse(entity.getMoneyChangerId(), entity.getCurrencyId())
                : repo.existsByMoneyChangerIdAndCurrencyIdAndIdNotAndIsDeletedFalse(entity.getMoneyChangerId(), entity.getCurrencyId(), entity.getId());

        if (exists) {
            throw new DuplicateResourceException("Money Changer Currency for the same money changer and currency already exists.");
        }

        return repo.save(entity);
    }

    @Override
    public void delete(Integer id, Integer userId) {
        MoneyChangerCurrency existing = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Money Changer Currency with ID " + id + " not found"));

        existing.setIsDeleted(true);  // Soft delete
        existing.setUpdatedBy(userId);
        repo.save(existing);
    }

    @Override
    public List<MoneyChangerCurrency> findByMoneyChangerId(Integer id) {
        return repo.findByMoneyChangerId_idAndIsDeletedFalse(id.longValue());
    }


}

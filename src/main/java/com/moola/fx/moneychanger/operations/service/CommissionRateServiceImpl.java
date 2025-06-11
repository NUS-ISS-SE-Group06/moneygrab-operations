package com.moola.fx.moneychanger.operations.service;

import com.moola.fx.moneychanger.operations.exception.DuplicateResourceException;
import com.moola.fx.moneychanger.operations.exception.ResourceNotFoundException;
import com.moola.fx.moneychanger.operations.model.CommissionRate;
import com.moola.fx.moneychanger.operations.repository.CommissionRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommissionRateServiceImpl implements CommissionRateService {
    private final CommissionRateRepository repo;

    @Autowired
    public CommissionRateServiceImpl(CommissionRateRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<CommissionRate> listAll() {
        return repo.findAll()
                .stream()
                .filter(rate -> Boolean.FALSE.equals(rate.getIsDeleted()))
                .toList();
    }

    @Override
    public CommissionRate get(Integer id) {
        return repo.findByIdAndIsDeletedFalse(id).orElseThrow(() -> new ResourceNotFoundException("Commission Rate with ID " + id + " not found  or has been deleted"));
    }


    @Override
    @Transactional
    public CommissionRate save(CommissionRate item) {
        boolean exists=(item.getId() == null)
                ? repo.existsByCurrencyIdAndSchemeIdAndIsDeletedFalse(item.getCurrencyId(), item.getSchemeId())
                : repo.existsByCurrencyIdAndSchemeIdAndIdNotAndIsDeletedFalse(item.getCurrencyId(), item.getSchemeId(), item.getId());

        if (exists) {
            throw new DuplicateResourceException("Commission rate for the same currency and scheme already exists.");
        }

        return repo.save(item);
    }

    @Override
    public void delete(Integer id, Integer userId) {
        CommissionRate existing = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Commission Rate with ID " + id + " not found"));

        existing.setIsDeleted(true);  // Soft delete
        existing.setUpdatedBy(userId);
        repo.save(existing);
    }

    @Override
    public List<CommissionRate> findBySchemeId(Integer schemeId) {
        return repo.findBySchemeIdIdAndIsDeletedFalse(schemeId);
    }


}
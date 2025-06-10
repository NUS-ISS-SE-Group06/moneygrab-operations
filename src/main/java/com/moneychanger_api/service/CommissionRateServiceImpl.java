package com.moneychanger_api.service;

import com.moneychanger_api.exception.DuplicateResourceException;
import com.moneychanger_api.exception.ResourceNotFoundException;
import com.moneychanger_api.model.CommissionRate;
import com.moneychanger_api.repository.CommissionRateRepository;
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
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Commission Rate with ID " + id + " not found"));
    }

    @Override
    @Transactional
    public CommissionRate save(CommissionRate item) {
        // Check for duplicate based on currency + scheme
        boolean exists = repo.existsByCurrencyIdAndSchemeIdAndIsDeletedFalse(item.getCurrencyId(), item.getSchemeId());
        if (exists) {
            throw new DuplicateResourceException("Commission rate for the same currency and scheme already exists.");
        }

        return repo.save(item);
    }

    @Override
    public void delete(Integer id) {
        CommissionRate commissionRate = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Commission Rate with ID " + id + " not found"));

        commissionRate.setIsDeleted(true);  // Soft delete
        repo.save(commissionRate);
    }

}
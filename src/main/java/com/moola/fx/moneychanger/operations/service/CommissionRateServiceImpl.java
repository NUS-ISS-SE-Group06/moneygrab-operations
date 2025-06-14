package com.moola.fx.moneychanger.operations.service;

import com.moola.fx.moneychanger.operations.exception.DuplicateResourceException;
import com.moola.fx.moneychanger.operations.exception.ForeignKeyConstraintException;
import com.moola.fx.moneychanger.operations.exception.ResourceNotFoundException;
import com.moola.fx.moneychanger.operations.model.CommissionRate;
import com.moola.fx.moneychanger.operations.repository.CommissionRateRepository;
import com.moola.fx.moneychanger.operations.repository.CompanyCommissionSchemeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommissionRateServiceImpl implements CommissionRateService {
    private final CommissionRateRepository repo;
    private final CompanyCommissionSchemeRepository companyCommissionSchemeRepo;

    @Autowired
    public CommissionRateServiceImpl(CommissionRateRepository repo, CompanyCommissionSchemeRepository companyCommissionSchemeRepo) {
        this.repo = repo;
        this.companyCommissionSchemeRepo = companyCommissionSchemeRepo;
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
    public CommissionRate save(CommissionRate entity) {
        boolean exists=(entity.getId() == null)
                ? repo.existsByCurrencyIdAndSchemeIdAndIsDeletedFalse(entity.getCurrencyId(), entity.getSchemeId())
                : repo.existsByCurrencyIdAndSchemeIdAndIdNotAndIsDeletedFalse(entity.getCurrencyId(), entity.getSchemeId(), entity.getId());

        if (exists) {
            throw new DuplicateResourceException("Commission rate for the same currency and scheme already exists.");
        }

        return repo.save(entity);
    }

    @Override
    public void delete(Integer id, Integer userId) {
        CommissionRate existing = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Commission Rate with ID " + id + " not found"));

        boolean schemeInUse=companyCommissionSchemeRepo.existsBySchemeId_IdAndIsDeletedFalse(existing.getSchemeId().getId());

        if (schemeInUse) {
            throw new ForeignKeyConstraintException("Cannot delete: scheme is still in use by a company commission scheme.");
        }

        existing.setIsDeleted(true);  // Soft delete
        existing.setUpdatedBy(userId);
        repo.save(existing);
    }

    @Override
    public List<CommissionRate> findBySchemeId(Integer id) {
        return repo.findBySchemeIdIdAndIsDeletedFalse(id);
    }


}
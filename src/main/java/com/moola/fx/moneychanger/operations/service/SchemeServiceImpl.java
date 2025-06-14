package com.moola.fx.moneychanger.operations.service;

import com.moola.fx.moneychanger.operations.exception.DuplicateResourceException;
import com.moola.fx.moneychanger.operations.exception.ForeignKeyConstraintException;
import com.moola.fx.moneychanger.operations.exception.ResourceNotFoundException;
import com.moola.fx.moneychanger.operations.model.Scheme;
import com.moola.fx.moneychanger.operations.repository.CompanyCommissionSchemeRepository;
import com.moola.fx.moneychanger.operations.repository.SchemeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SchemeServiceImpl implements SchemeService {

    private final CompanyCommissionSchemeRepository companyCommissionSchemeRepo;
    private SchemeRepository repo;


    @Autowired
    public SchemeServiceImpl(SchemeRepository repo, CompanyCommissionSchemeRepository companyCommissionSchemeRepo) {
        this.repo = repo;
        this.companyCommissionSchemeRepo = companyCommissionSchemeRepo;
    }

    @Override
    public List<Scheme> listAll() {
        return repo.findAll().stream()
                .filter(s -> !Boolean.TRUE.equals(s.getIsDeleted()))
                .toList();
    }

    @Override
    public Scheme get(Integer id) {
        return repo.findByIdAndIsDeletedFalse(id).orElseThrow(() -> new ResourceNotFoundException("Commission Rate with ID " + id + " not found  or has been deleted"));
    }

    @Override
    @Transactional
    public Scheme save(Scheme entity) {
        String normalizedName = (entity.getNameTag() == null)
                ? ""
                : entity.getNameTag().trim().toLowerCase();

        boolean exists = (entity.getId() == null)
                ? repo.existsByNameTagIgnoreCaseAndIsDeletedFalse(normalizedName)
                : repo.existsByNameTagIgnoreCaseAndIdNotAndIsDeletedFalse(normalizedName, entity.getId());

        if (exists) {
            throw new DuplicateResourceException("Scheme with name '" + entity.getNameTag().trim() + "' already exists");
        }

        // Ensure only one default scheme
        if (Boolean.TRUE.equals(entity.getIsDefault())) {
            repo.findAll().stream()
                    .filter(s -> Boolean.TRUE.equals(s.getIsDefault()) && !s.getId().equals(entity.getId()))
                    .forEach(s -> {
                        s.setIsDefault(false);
                        repo.save(s);
                    });
        }

        return repo.save(entity);
    }

    @Override
    public void delete(Integer id, Integer userId) {
        Scheme existing = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Scheme with ID " + id + " not found"));

        boolean schemeInUse=companyCommissionSchemeRepo.existsBySchemeId_IdAndIsDeletedFalse(existing.getId());

        if (schemeInUse) {
            throw new ForeignKeyConstraintException("Cannot delete: scheme is still in use by a company commission scheme.");
        }
        existing.setIsDeleted(true);  // Soft delete
        existing.setUpdatedBy(userId);
        repo.save(existing);
    }



}
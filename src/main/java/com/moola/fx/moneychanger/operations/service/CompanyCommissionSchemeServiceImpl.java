package com.moola.fx.moneychanger.operations.service;

import com.moola.fx.moneychanger.operations.dto.CompanyCommissionSchemeDTO;
import com.moola.fx.moneychanger.operations.exception.DuplicateResourceException;
import com.moola.fx.moneychanger.operations.exception.ResourceNotFoundException;
import com.moola.fx.moneychanger.operations.mapper.CompanyCommissionSchemeMapper;
import com.moola.fx.moneychanger.operations.model.CompanyCommissionScheme;
import com.moola.fx.moneychanger.operations.model.MoneyChanger;
import com.moola.fx.moneychanger.operations.repository.CompanyCommissionSchemeRepository;
import com.moola.fx.moneychanger.operations.repository.MoneyChangerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CompanyCommissionSchemeServiceImpl implements CompanyCommissionSchemeService {

    private final CompanyCommissionSchemeRepository repo;
    private final MoneyChangerRepository moneyChangerRepo;

    @Autowired
    public CompanyCommissionSchemeServiceImpl(
            CompanyCommissionSchemeRepository repo,
            MoneyChangerRepository moneyChangerRepo,
            CompanyCommissionSchemeMapper mapper
    ) {
        this.repo = repo;
        this.moneyChangerRepo = moneyChangerRepo;
    }

    @Override
    public List<CompanyCommissionScheme> listAll() {
        return repo.findAll()
                .stream()
                .filter(scheme -> Boolean.FALSE.equals(scheme.getIsDeleted()))
                .toList();
    }

    @Override
    public CompanyCommissionScheme get(Integer id) {
        return repo.findByIdAndIsDeletedFalse(id).orElseThrow(() -> new ResourceNotFoundException("Company Commission Scheme with ID " + id + " not found  or has been deleted"));
    }

    @Override
    @Transactional
    public CompanyCommissionScheme save(CompanyCommissionScheme entity) {

        boolean exists=(entity.getId() == null)
                ? repo.existsByMoneyChangerId_idAndSchemeId_IdAndIsDeletedFalse(entity.getMoneyChangerId().getId(), entity.getSchemeId().getId())
                : repo.existsByMoneyChangerId_IdAndSchemeId_IdAndIdNotAndIsDeletedFalse(entity.getMoneyChangerId().getId(), entity.getSchemeId().getId(), entity.getId());

        if (exists) {
            throw new DuplicateResourceException("Company Commission Scheme for the same moneyChanger and scheme already exists.");
        }

        return repo.save(entity);
    }

    @Override
    @Transactional
    public CompanyCommissionScheme save(CompanyCommissionSchemeDTO dto) {
        CompanyCommissionScheme existing = this.get(dto.getId());
        MoneyChanger ref = moneyChangerRepo.getReferenceById(dto.getMoneyChangerId().longValue());
        existing.setMoneyChangerId(ref);
        existing.setUpdatedBy(dto.getUpdatedBy());
        return save(existing);
    }

    @Override
    public void delete(Integer id, Integer userId) {
        CompanyCommissionScheme existing = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company Commission Scheme with ID " + id + " not found"));

        existing.setIsDeleted(true);  // Soft delete
        existing.setUpdatedBy(userId);
        repo.save(existing);
    }

    @Override
    public List<CompanyCommissionScheme> findBySchemeId(Integer id) {
        return repo.findBySchemeId_idAndIsDeletedFalse(id);
    }

}
package com.moola.fx.moneychanger.operations.service;

import com.moola.fx.moneychanger.operations.exception.DuplicateResourceException;
import com.moola.fx.moneychanger.operations.exception.ResourceNotFoundException;
import com.moola.fx.moneychanger.operations.model.CompanyCommissionScheme;
import com.moola.fx.moneychanger.operations.repository.CommissionRateRepository;
import com.moola.fx.moneychanger.operations.repository.CompanyCommissionSchemeRepository;
import com.moola.fx.moneychanger.operations.repository.MoneyChangerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyCommissionSchemeServiceImpl implements CompanyCommissionSchemeService {

    private final CompanyCommissionSchemeRepository repo;
    private final CommissionRateRepository commissionRateRepo;
    private final MoneyChangerRepository moneyChangerRepo;


    @Autowired
    public CompanyCommissionSchemeServiceImpl(
            CompanyCommissionSchemeRepository repo,
            CommissionRateRepository commissionRateRepo,
            MoneyChangerRepository moneyChangerRepo
    ) {
        this.repo = repo;
        this.commissionRateRepo = commissionRateRepo;
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
        CompanyCommissionScheme scheme = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company Commission Scheme with ID " + id + " not found"));

        if (Boolean.TRUE.equals(scheme.getIsDeleted())) {
            throw new ResourceNotFoundException("Company Commission Scheme with ID " + id + " not found");
        }

        return scheme;
    }

    @Override
    public CompanyCommissionScheme save(CompanyCommissionScheme item) {
        if (item.getMoneyChangerId() == null || item.getCommissionRateId() == null) {
            throw new IllegalArgumentException("moneyChangerId and commissionRateId must not be null");
        }

        if (!moneyChangerRepo.existsById(item.getMoneyChangerId().getId())) {
            throw new ResourceNotFoundException("MoneyChanger with ID " + item.getMoneyChangerId().getId() + " not found");
        }

        if (!commissionRateRepo.existsById(item.getCommissionRateId().getId())) {
            throw new ResourceNotFoundException("CommissionRate with ID " + item.getCommissionRateId().getId() + " not found");
        }

        boolean exists = repo.existsByMoneyChangerId_IdAndCommissionRateId_Id(
                item.getMoneyChangerId().getId(),
                item.getCommissionRateId().getId()
        );


        if (exists) {
            throw new DuplicateResourceException("Company Commission Scheme for moneyChangerId '"
                    + item.getMoneyChangerId().getId() + "' and commissionRateId '"
                    + item.getCommissionRateId().getId() + "' already exists");
        }

        return repo.save(item);
    }

    @Override
    public void delete(Integer id) {
        CompanyCommissionScheme companyCommissionScheme = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company Commission Scheme with ID " + id + " not found"));

        companyCommissionScheme.setIsDeleted(true);  // Soft delete
        repo.save(companyCommissionScheme);

    }
}
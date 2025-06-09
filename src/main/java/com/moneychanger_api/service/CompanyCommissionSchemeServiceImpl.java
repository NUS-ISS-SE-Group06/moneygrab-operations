package com.moneychanger_api.service;

import com.moneychanger_api.exception.DuplicateResourceException;
import com.moneychanger_api.exception.ResourceNotFoundException;
import com.moneychanger_api.model.CompanyCommissionScheme;
import com.moneychanger_api.repository.CommissionRateRepository;
import com.moneychanger_api.repository.CompanyCommissionSchemeRepository;
import com.moneychanger_api.repository.MoneyChangerRepository;
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
        // Validate non-null references
        if (item.getMoneyChangerId() == null || item.getCommissionRateId() == null) {
            throw new IllegalArgumentException("moneyChangerId and commissionRateId must not be null");
        }

        // Validate that referenced money changer exists
        if (!moneyChangerRepo.existsById(item.getMoneyChangerId().getId())) {
            throw new ResourceNotFoundException("MoneyChanger with ID " + item.getMoneyChangerId() + " not found");
        }

        // Validate that referenced commission rate exists
        if (!commissionRateRepo.existsById(item.getCommissionRateId().getId())) {
            throw new ResourceNotFoundException("CommissionRate with ID " + item.getCommissionRateId() + " not found");
        }

        // Prevent multiple configurations per money_changer_id
        boolean exists = repo.findAll().stream()
                .anyMatch(s -> s.getMoneyChangerId().equals(item.getMoneyChangerId()));

        if (exists) {
            throw new DuplicateResourceException("Company Commission Scheme for money_changer_id '"
                    + item.getMoneyChangerId() + "' already exists");
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
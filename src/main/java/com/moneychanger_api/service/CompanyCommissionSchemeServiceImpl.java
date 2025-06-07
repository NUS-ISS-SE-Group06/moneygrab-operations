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
    @Autowired
    private CompanyCommissionSchemeRepository repo;

    @Autowired
    private CommissionRateRepository commissionraterepo;

    @Autowired
    private MoneyChangerRepository moneychangerrepo;

    public List<CompanyCommissionScheme> listAll() { return repo.findAll(); }
    public CompanyCommissionScheme get(Integer id) { return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Company Commission Scheme with ID " + id + " not found")); }
    public CompanyCommissionScheme save(CompanyCommissionScheme item) {
        // Validate non-null references
        if (item.getMoneyChangerId() == null || item.getCommissionRateId() == null) {
            throw new IllegalArgumentException("moneyChangerId and commissionRateId must not be null");
        }

        // Validate that referenced money changer exists
        if (!moneychangerrepo.existsById(item.getMoneyChangerId().getId())) {
            throw new ResourceNotFoundException("MoneyChanger with ID " + item.getMoneyChangerId() + " not found");
        }

        // Validate that referenced commission rate exists
        if (!commissionraterepo.existsById(item.getCommissionRateId().getId())) {
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
    public void delete(Integer id) {
        CompanyCommissionScheme companyCommissionScheme = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Company Commission Scheme with ID " + id + " not found"));

        companyCommissionScheme.setIsDeleted(true);  // Soft delete
        repo.save(companyCommissionScheme);

    }
}
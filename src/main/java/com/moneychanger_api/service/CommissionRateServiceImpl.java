package com.moneychanger_api.service;

import com.moneychanger_api.exception.DuplicateResourceException;
import com.moneychanger_api.exception.ResourceNotFoundException;
import com.moneychanger_api.model.CommissionRate;
import com.moneychanger_api.repository.CommissionRateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommissionRateServiceImpl implements CommissionRateService {
    @Autowired
    private CommissionRateRepository repo;

    public List<CommissionRate> listAll() { return repo.findAll(); }
    public CommissionRate get(Integer id) { return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Commission Rate with ID " + id + " not found")); }
    public CommissionRate save(CommissionRate item) {
        String normalizedName = item.getDescription().trim();

        // Check for existing commission rate with the same description
        if (repo.existsByDescriptionIgnoreCase(normalizedName)) {
            throw new DuplicateResourceException("Commission rate with description '" + normalizedName + "' already exists");
        }
        return repo.save(item);
    }
    public void delete(Integer id) {
        CommissionRate commissionRate = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Commission Rate with ID " + id + " not found"));

        commissionRate.setIsDeleted(true);  // Soft delete
        repo.save(commissionRate);
    }
}
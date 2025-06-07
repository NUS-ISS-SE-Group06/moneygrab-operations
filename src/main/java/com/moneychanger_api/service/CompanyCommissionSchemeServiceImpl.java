package com.moneychanger_api.service;

import com.moneychanger_api.exception.ResourceNotFoundException;
import com.moneychanger_api.model.CompanyCommissionScheme;
import com.moneychanger_api.repository.CompanyCommissionSchemeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyCommissionSchemeServiceImpl implements CompanyCommissionSchemeService {
    @Autowired
    private CompanyCommissionSchemeRepository repo;

    public List<CompanyCommissionScheme> listAll() { return repo.findAll(); }
    public CompanyCommissionScheme get(Integer id) { return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Company Commission Scheme with ID " + id + " not found")); }
    public CompanyCommissionScheme save(CompanyCommissionScheme item) { return repo.save(item); }
    public void delete(Integer id) { repo.deleteById(id); }
}
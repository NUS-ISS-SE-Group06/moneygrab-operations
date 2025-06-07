package com.moneychanger_api.service;

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
    public CommissionRate get(Integer id) { return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("CommissionRate with ID " + id + " not found")); }
    public CommissionRate save(CommissionRate item) { return repo.save(item); }
    public void delete(Integer id) { repo.deleteById(id); }
}
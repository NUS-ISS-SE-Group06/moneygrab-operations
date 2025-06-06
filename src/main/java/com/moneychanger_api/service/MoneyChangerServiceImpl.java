package com.moneychanger_api.service;

import com.moneychanger_api.model.MoneyChanger;
import com.moneychanger_api.repository.MoneyChangerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MoneyChangerServiceImpl implements MoneyChangerService {

    @Autowired
    private MoneyChangerRepository repository;

    @Override
    public List<MoneyChanger> getAll() {
        return repository.findByIsDeletedFalse();
    }

    @Override
    public MoneyChanger getById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public MoneyChanger create(MoneyChanger moneyChanger) {
        // Manually set default fields to avoid null column errors
        moneyChanger.setCreatedAt(LocalDateTime.now());
        moneyChanger.setUpdatedAt(LocalDateTime.now());
        moneyChanger.setIsDeleted(false);
        return repository.save(moneyChanger);
    }

    @Override
    public MoneyChanger update(Long id, MoneyChanger updated) {
        MoneyChanger existing = repository.findById(id).orElseThrow();
        existing.setCompanyName(updated.getCompanyName());
        existing.setEmail(updated.getEmail());
        existing.setAddress(updated.getAddress());
        existing.setPostalCode(updated.getPostalCode());
        existing.setNotes(updated.getNotes());
        existing.setUpdatedAt(LocalDateTime.now()); // Also update the timestamp
        return repository.save(existing);
    }

    @Override
    public void delete(Long id, String role) {
        if (!"admin".equals(role)) {
            throw new RuntimeException("Only admin can delete staff.");
        }
        MoneyChanger changer = repository.findById(id).orElseThrow();
        changer.setIsDeleted(true);
        changer.setUpdatedAt(LocalDateTime.now());
        repository.save(changer);
    }
}

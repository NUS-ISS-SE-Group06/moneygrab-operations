package com.moneychanger_api.service;

import com.moneychanger_api.exception.DuplicateResourceException;
import com.moneychanger_api.exception.ResourceNotFoundException;
import com.moneychanger_api.model.Scheme;
import com.moneychanger_api.repository.SchemeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SchemeServiceImpl implements SchemeService {

    private SchemeRepository repo;

    @Autowired
    public SchemeServiceImpl(SchemeRepository repo) {
        this.repo = repo;
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
    public Scheme save(Scheme item) {
        String normalizedName = (item.getNameTag() == null)
                ? ""
                : item.getNameTag().trim().toLowerCase();

        boolean exists = (item.getId() == null)
                ? repo.existsByNameTagIgnoreCaseAndIsDeletedFalse(normalizedName)
                : repo.existsByNameTagIgnoreCaseAndIdNotAndIsDeletedFalse(normalizedName, item.getId());

        if (exists) {
            throw new DuplicateResourceException("Scheme with name '" + item.getNameTag().trim() + "' already exists");
        }

        // Ensure only one default scheme
        if (Boolean.TRUE.equals(item.getIsDefault())) {
            repo.findAll().stream()
                    .filter(s -> Boolean.TRUE.equals(s.getIsDefault()) && !s.getId().equals(item.getId()))
                    .forEach(s -> {
                        s.setIsDefault(false);
                        repo.save(s);
                    });
        }

        return repo.save(item);
    }

    @Override
    public void delete(Integer id, Integer userId) {
        Scheme existing = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Scheme with ID " + id + " not found"));

        existing.setIsDeleted(true);  // Soft delete
        existing.setUpdatedBy(userId);
        repo.save(existing);
    }



}
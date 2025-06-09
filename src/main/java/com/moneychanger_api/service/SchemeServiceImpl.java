package com.moneychanger_api.service;

import com.moneychanger_api.exception.DuplicateResourceException;
import com.moneychanger_api.exception.ForeignKeyConstraintException;
import com.moneychanger_api.exception.ResourceNotFoundException;
import com.moneychanger_api.model.Scheme;
import com.moneychanger_api.repository.SchemeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
    public List<Scheme> listAll() { return repo.findAll(); }

    @Override
    public Scheme get(Integer id) { return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Scheme with ID " + id + " not found")); }

    @Override
    public Scheme save(Scheme item) {
        String normalizedName = item.getName().toLowerCase();

        // Check for existing scheme with the same name
        boolean exists = repo.findAll().stream()
                .anyMatch(s -> s.getName().trim().equalsIgnoreCase(normalizedName));

        if (exists) {
            throw new DuplicateResourceException("Scheme with name '" + item.getName().trim() + "' already exists");
        }

        // If item is marked as default, unset all other default schemes
        if (item.getIsDefault() != null && item.getIsDefault()) {
            List<Scheme> allSchemes = repo.findAll();
            for (Scheme scheme : allSchemes) {
                if (scheme.getIsDefault() != null && scheme.getIsDefault()) {
                    scheme.setIsDefault(false);
                    repo.save(scheme);  // update existing default to false
                }
            }
        }

        return repo.save(item);
    }

    @Override
    public void delete(Integer id) {
        try {
            repo.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new ForeignKeyConstraintException("Scheme is in use and cannot be deleted.");

        }
    }
}
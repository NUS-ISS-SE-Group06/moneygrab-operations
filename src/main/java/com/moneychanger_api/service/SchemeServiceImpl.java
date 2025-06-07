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
    @Autowired
    private SchemeRepository repo;

    public List<Scheme> listAll() { return repo.findAll(); }
    public Scheme get(Integer id) { return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Scheme with ID " + id + " not found")); }
    public Scheme save(Scheme item) {
        String normalizedName = item.getName().toLowerCase();

        // Check for existing scheme with the same name
        boolean exists = repo.findAll().stream()
                .anyMatch(s -> s.getName().trim().equalsIgnoreCase(normalizedName));

        if (exists) {
            throw new DuplicateResourceException("Scheme with name '" + item.getName().trim() + "' already exists");
        }

        return repo.save(item);
    }
    public void delete(Integer id) {
        try {
            repo.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new ForeignKeyConstraintException("Scheme is in use and cannot be deleted.");

        }
    }
}
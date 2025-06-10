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
        Scheme scheme = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Scheme with ID " + id + " not found"));

        if (Boolean.TRUE.equals(scheme.getIsDeleted())) {
            throw new ResourceNotFoundException("Scheme with ID " + id + " is marked as deleted.");
        }

        return scheme;
    }

    @Override
    public Scheme save(Scheme item) {
        String normalizedName = item.getNameTag().toLowerCase();

        boolean exists = repo.findAll().stream()
                .filter(s -> !Boolean.TRUE.equals(s.getIsDeleted()))
                .anyMatch(s -> !s.getId().equals(item.getId())
                        && s.getNameTag().trim().equalsIgnoreCase(normalizedName));

        if (exists) {
            throw new DuplicateResourceException("Scheme with name '" + item.getNameTag().trim() + "' already exists");
        }

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
    public void delete(Integer id, Integer who) {
        int updated = repo.markDeletedById(id, who);
        if (updated == 0) {
            throw new ResourceNotFoundException("Scheme with ID " + id + " not found");
        }

    }





}
package com.moneychanger_api.service;

import com.moneychanger_api.exception.ResourceNotFoundException;
import com.moneychanger_api.model.Scheme;
import com.moneychanger_api.repository.SchemeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SchemeServiceImpl implements SchemeService {
    @Autowired
    private SchemeRepository repo;

    public List<Scheme> listAll() { return repo.findAll(); }
    public Scheme get(Integer id) { return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Scheme with ID " + id + " not found")); }
    public Scheme save(Scheme item) { return repo.save(item); }
    public void delete(Integer id) { repo.deleteById(id); }
}
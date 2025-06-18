package com.moola.fx.moneychanger.operations.service;

import com.moola.fx.moneychanger.operations.exception.ResourceNotFoundException;
import com.moola.fx.moneychanger.operations.model.CurrencyCode;
import com.moola.fx.moneychanger.operations.repository.CurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CurrencyServiceImpl implements CurrencyService {

    private CurrencyRepository repo;

    @Autowired
    public CurrencyServiceImpl(CurrencyRepository repo) {
        this.repo = repo;
    }

    @Override
    public List<CurrencyCode> listAll() {
        return repo.findAll().stream()
                .toList();
    }

    @Override
    public CurrencyCode get(Integer id) {
        return repo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Currency Code with ID " + id + " not found  or has been deleted"));
    }

    @Override
    public CurrencyCode getByCurrency(String currency) {
        return repo.findByCurrencyIgnoreCase(currency).orElseThrow(() -> new ResourceNotFoundException("Currency Code with " + currency + " not found  or has been deleted"));
    }




}

package com.moola.fx.moneychanger.operations.service;

import com.moola.fx.moneychanger.operations.model.MoneyChanger;
import com.moola.fx.moneychanger.operations.repository.MoneyChangerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MoneyChangerServiceImpl implements MoneyChangerService {

    private static final String NOT_FOUND_MSG = "not found";
    private static final String MONEYCHANGER_ID_MSG = "MoneyChanger with ID ";

    @Autowired
    private MoneyChangerRepository repository;

    @Override
    public List<MoneyChanger> getAll() {
        return repository.findByIsDeletedFalse();
    }

    @Override
    public MoneyChanger getById(Long id) {
        return repository.findById(id)
                .filter(mc -> !Boolean.TRUE.equals(mc.getIsDeleted()))
                .orElseThrow(() ->
                        new EntityNotFoundException(MONEYCHANGER_ID_MSG + id + " " + NOT_FOUND_MSG));
    }

    @Override
    public MoneyChanger create(MoneyChanger moneyChanger) {
        moneyChanger.setIsDeleted(false);
        return repository.save(moneyChanger);
    }

    @Override
    public MoneyChanger update(Long id, MoneyChanger updatedMoneyChanger) {
        MoneyChanger existing = repository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(MONEYCHANGER_ID_MSG + id + " " + NOT_FOUND_MSG));

        existing.setCompanyName(updatedMoneyChanger.getCompanyName());
        existing.setEmail(updatedMoneyChanger.getEmail());
        existing.setAddress(updatedMoneyChanger.getAddress());
        existing.setPostalCode(updatedMoneyChanger.getPostalCode());
        existing.setNotes(updatedMoneyChanger.getNotes());
        existing.setDateOfIncorporation(updatedMoneyChanger.getDateOfIncorporation());
        existing.setCountry(updatedMoneyChanger.getCountry());
        existing.setUen(updatedMoneyChanger.getUen());
        existing.setSchemeId(updatedMoneyChanger.getSchemeId());

        return repository.save(existing);
    }

    @Override
    public void delete(Long id) {
        MoneyChanger mc = repository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(MONEYCHANGER_ID_MSG + id + " " + NOT_FOUND_MSG));

        mc.setIsDeleted(true);
        repository.save(mc);
    }

    @Override
    public MoneyChanger getOne(Long id) {
        return repository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(MONEYCHANGER_ID_MSG + id + " " + NOT_FOUND_MSG));
    }
}

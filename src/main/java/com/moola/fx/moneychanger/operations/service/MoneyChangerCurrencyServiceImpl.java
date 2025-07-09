package com.moola.fx.moneychanger.operations.service;

import com.moola.fx.moneychanger.operations.exception.DuplicateResourceException;
import com.moola.fx.moneychanger.operations.exception.ResourceNotFoundException;
import com.moola.fx.moneychanger.operations.model.ComputeRate;
import com.moola.fx.moneychanger.operations.model.ComputeRateId;
import com.moola.fx.moneychanger.operations.model.MoneyChanger;
import com.moola.fx.moneychanger.operations.model.MoneyChangerCurrency;
import com.moola.fx.moneychanger.operations.repository.MoneyChangerCurrencyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MoneyChangerCurrencyServiceImpl implements MoneyChangerCurrencyService {

    private final MoneyChangerCurrencyRepository repo;
    private final ComputeRateService computeRateService;
    private final CurrencyService currencyService;

    @Autowired
    public MoneyChangerCurrencyServiceImpl(MoneyChangerCurrencyRepository repo, ComputeRateService computeRateService, CurrencyService currencyService ) {
        this.repo = repo;
        this.currencyService = currencyService;
        this.computeRateService = computeRateService;
    }

    @Override
    public List<MoneyChangerCurrency> listAll() {
        return repo.findByIsDeletedFalse();
    }

    @Override
    public MoneyChangerCurrency get(Integer id) {
        return repo.findByIdAndIsDeletedFalse(id).orElseThrow(() -> new ResourceNotFoundException("Money Changer Currency with ID " + id + " not found  or has been deleted"));
    }


    @Override
    @Transactional
    public MoneyChangerCurrency save(MoneyChangerCurrency entity) {
        boolean exists=(entity.getId() == null)
                ? repo.existsByMoneyChangerIdAndCurrencyIdAndIsDeletedFalse(entity.getMoneyChangerId(), entity.getCurrencyId())
                : repo.existsByMoneyChangerIdAndCurrencyIdAndIdNotAndIsDeletedFalse(entity.getMoneyChangerId(), entity.getCurrencyId(), entity.getId());

        if (exists) {
            throw new DuplicateResourceException("Money Changer Currency for the same money changer and currency already exists.");
        }

        MoneyChangerCurrency saved= repo.save(entity);


        Long moneyChangerId = saved.getMoneyChangerId().getId();
        Integer  currencyId = saved.getCurrencyId().getId();
        String currencyCode = currencyService.get(currencyId).getCurrency();
        try {
            computeRateService.get(currencyCode, moneyChangerId);
        } catch (ResourceNotFoundException ex) {
            ComputeRate rate = new ComputeRate();

            ComputeRateId rateId = new ComputeRateId();
            rateId.setCurrencyCode(currencyCode);
            rateId.setMoneyChangerId(moneyChangerId);

            MoneyChanger moneychanger = new MoneyChanger();
            moneychanger.setId(moneyChangerId);

            rate.setId(rateId);
            rate.setMoneyChanger(moneychanger);

            rate.setProcessedBy(entity.getCreatedBy());

            rate.setProcessedBy(entity.getCreatedBy());

           computeRateService.saveAll(List.of(rate));
        }

        computeRateService.deleteOrphanedRates(moneyChangerId);

        return saved;
    }

    @Override
    @Transactional
    public void delete(Integer id, Integer userId) {
        MoneyChangerCurrency existing = repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Money Changer Currency with ID " + id + " not found"));

        existing.setIsDeleted(true);  // Soft delete
        existing.setUpdatedBy(userId);
        repo.save(existing);

        // Delete associated ComputeRate records
        String currencyCode = existing.getCurrencyId().getCurrency();
        Long moneyChangerId = existing.getMoneyChangerId().getId();

        computeRateService.findByMoneyChangerId(moneyChangerId).stream()
                .filter(rate -> rate.getId().getCurrencyCode().equals(currencyCode))
                .forEach(rate -> computeRateService.delete(currencyCode, moneyChangerId));
    }

    @Override
    public List<MoneyChangerCurrency> findByMoneyChangerId(Integer id) {
        return repo.findByMoneyChangerId_idAndIsDeletedFalse(id.longValue());
    }


}

package com.moola.fx.moneychanger.operations.service;
import com.moola.fx.moneychanger.operations.model.MoneyChanger;
import com.moola.fx.moneychanger.operations.repository.MoneyChangerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
@Service
public class MoneyChangerServiceImpl implements MoneyChangerService {
    private static final String NOT_FOUND_MESSAGE = "MoneyChanger with ID %d not found";
    private final MoneyChangerRepository repository;
    @Autowired
    public MoneyChangerServiceImpl(MoneyChangerRepository repository) {
        this.repository = repository;
    }
    @Override
    public List<MoneyChanger> getAll() {
        return repository.findByIsDeletedFalse();
    }
    @Override
    public MoneyChanger getOne(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(NOT_FOUND_MESSAGE, id)));
    }
    @Override
    public MoneyChanger create(MoneyChanger moneyChanger) {
        moneyChanger.setCreatedAt(LocalDateTime.now());
        moneyChanger.setUpdatedAt(LocalDateTime.now());
        moneyChanger.setIsDeleted(false);
        return repository.save(moneyChanger);
    }
    @Override
    public MoneyChanger update(Long id, MoneyChanger updated) {
        MoneyChanger existing = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(NOT_FOUND_MESSAGE, id)));
        existing.setCompanyName(updated.getCompanyName());
        existing.setEmail(updated.getEmail());
        existing.setAddress(updated.getAddress());
        existing.setPostalCode(updated.getPostalCode());
        existing.setUpdatedAt(LocalDateTime.now());
        return repository.save(existing);
    }
    @Override
    public void delete(Long id) {
        MoneyChanger changer = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format(NOT_FOUND_MESSAGE, id)));
        changer.setIsDeleted(true);
        changer.setUpdatedAt(LocalDateTime.now());
        repository.save(changer);
    }
    @Override
    public MoneyChanger getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException(String.format(NOT_FOUND_MESSAGE, id)));
    }
}
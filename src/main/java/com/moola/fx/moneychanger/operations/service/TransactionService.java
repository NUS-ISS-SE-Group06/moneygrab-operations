package com.moola.fx.moneychanger.operations.service;

import com.moola.fx.moneychanger.operations.model.Transaction;
import com.moola.fx.moneychanger.operations.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService {

    private final TransactionRepository repository;

    public TransactionService(TransactionRepository repository) {
        this.repository = repository;
    }

    // public List<Transaction> getAllTransactions() {
    //     return repository.findAll();
    // }
    public List<Transaction> listByMoneyChanger(int moneyChangerId) {
        return repository.findByMoneyChangerId(moneyChangerId);
    }
}

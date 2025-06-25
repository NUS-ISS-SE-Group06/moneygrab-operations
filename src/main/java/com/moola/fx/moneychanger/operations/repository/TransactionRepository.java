package com.moola.fx.moneychanger.operations.repository;

import com.moola.fx.moneychanger.operations.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    // You can define custom queries here if needed, e.g.:
    // List<Transaction> findByCustomerId(int customerId);
}

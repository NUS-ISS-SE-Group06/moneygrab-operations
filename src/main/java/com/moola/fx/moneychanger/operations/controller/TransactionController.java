package com.moola.fx.moneychanger.operations.controller;

import com.moola.fx.moneychanger.operations.model.Transaction;
import com.moola.fx.moneychanger.operations.service.TransactionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/transactions")
public class TransactionController {

    private final TransactionService service;
    
  
    public TransactionController(TransactionService service) {
        this.service = service;
    }

    /**
     * GET /api/transactions
     * Returns all transactions as JSON.
     */
    @GetMapping
    public List<Transaction> listTransactions() {
        return service.getAllTransactions();
    }
}

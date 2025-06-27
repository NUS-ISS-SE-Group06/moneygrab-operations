package com.moola.fx.moneychanger.operations.controller;

import com.moola.fx.moneychanger.operations.model.Transaction;
import com.moola.fx.moneychanger.operations.service.TransactionService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/transaction")
public class TransactionController {

    private final TransactionService service;
    
  
    public TransactionController(TransactionService service) {
        this.service = service;
    }

    /**
     * GET /api/transaction
     * Returns all transactions as JSON.
     */
    @GetMapping
   public ResponseEntity<?> getByMoneyChanger(@RequestParam int moneyChangerId) {
        List<Transaction> transactions = service.listByMoneyChanger(moneyChangerId);

        if (transactions == null || transactions.isEmpty()) {
            return ResponseEntity
                    .status(404)
                    .body("Records not found");
        }

        return ResponseEntity.ok(transactions);
    }
}

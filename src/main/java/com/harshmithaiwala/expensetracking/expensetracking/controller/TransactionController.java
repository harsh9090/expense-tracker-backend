package com.harshmithaiwala.expensetracking.expensetracking.controller;

import com.harshmithaiwala.expensetracking.expensetracking.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    // ✅ Get All Transactions for Logged-in User
    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getTransactions(Authentication authentication) {
        return ResponseEntity.ok(transactionService.getTransactions(authentication.getName()));
    }
}

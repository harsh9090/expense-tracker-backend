package com.harshmithaiwala.expensetracking.expensetracking.controller;

import com.harshmithaiwala.expensetracking.expensetracking.model.Income;
import com.harshmithaiwala.expensetracking.expensetracking.model.User;
import com.harshmithaiwala.expensetracking.expensetracking.repo.IncomeRepo;
import com.harshmithaiwala.expensetracking.expensetracking.repo.UserRepo;
import com.harshmithaiwala.expensetracking.expensetracking.service.IncomeService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/income")
public class IncomeController {

    private final IncomeService incomeService;
    private final UserRepo userRepo;
    private final IncomeRepo incomeRepo;

    public IncomeController(IncomeService incomeService, UserRepo userRepo, IncomeRepo incomeRepo) {
        this.incomeService = incomeService;
        this.userRepo = userRepo;
        this.incomeRepo = incomeRepo;
    }

    // ✅ Add Income
    @PostMapping
    public ResponseEntity<Map<String, Object>> addIncome(@RequestBody Income income, Authentication authentication) {
        Income savedIncome = incomeService.addIncome(income, authentication.getName());
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Income added successfully");
        response.put("id", savedIncome.getId().toString());
        response.put("amount", savedIncome.getAmount());
        response.put("category", savedIncome.getCategory());
        response.put("source", savedIncome.getSource());
        return ResponseEntity.ok(response);
    }

    // ✅ Get All Income for Logged-in User
    @GetMapping
    public ResponseEntity<List<Income>> getIncome(Authentication authentication) {
        return ResponseEntity.ok(incomeService.getIncome(authentication.getName()));
    }

    // ✅ Update Income
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateIncome(
            @PathVariable UUID id,
            @RequestBody Income income,
            Authentication authentication) {
        Income updatedIncome = incomeService.updateIncome(id, income, authentication.getName());
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Income updated successfully");
        response.put("id", updatedIncome.getId().toString());
        response.put("amount", updatedIncome.getAmount());
        response.put("category", updatedIncome.getCategory());
        response.put("source", updatedIncome.getSource());
        return ResponseEntity.ok(response);
    }

    // ✅ Delete Income
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteIncome(@PathVariable UUID id, Authentication authentication) {
        incomeService.deleteIncome(id, authentication.getName());
        Map<String, String> response = new HashMap<>();
        response.put("message", "Income deleted successfully");
        response.put("id", id.toString());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<Map<String, Object>> getIncomeByCategory(
            @PathVariable String category,
            Authentication authentication) {
        User user = userRepo.findByEmail(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Double total = incomeRepo.getTotalByCategory(user, category);
        Map<String, Object> response = new HashMap<>();
        response.put("category", category);
        response.put("total", total);
        return ResponseEntity.ok(response);
    }

    // Get Monthly Income
    @GetMapping("/monthly/{month}/{year}")
    public ResponseEntity<List<Income>> getMonthlyIncome(
            @PathVariable int month,
            @PathVariable int year,
            Authentication authentication) {
        return ResponseEntity.ok(incomeService.getMonthlyIncome(authentication.getName(), year, month));
    }
}

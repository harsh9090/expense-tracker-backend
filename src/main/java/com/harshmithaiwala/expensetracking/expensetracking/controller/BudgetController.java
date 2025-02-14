package com.harshmithaiwala.expensetracking.expensetracking.controller;

import com.harshmithaiwala.expensetracking.expensetracking.model.Budget;
import com.harshmithaiwala.expensetracking.expensetracking.service.BudgetService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/budgets")
public class BudgetController {

    private final BudgetService budgetService;

    public BudgetController(BudgetService budgetService) {
        this.budgetService = budgetService;
    }

    // ✅ Add or Update Budget
    @PostMapping
    public ResponseEntity<Budget> saveBudget(@RequestBody Budget budget, Authentication authentication) {
        return ResponseEntity.ok(budgetService.saveBudget(budget, authentication.getName()));
    }

    // ✅ Get All Budgets for Logged-in User
    @GetMapping
    public ResponseEntity<List<Budget>> getBudgets(Authentication authentication) {
        return ResponseEntity.ok(budgetService.getBudgets(authentication.getName()));
    }

    // ✅ Update Budget
    @PutMapping("/{id}")
    public ResponseEntity<Budget> updateBudget(@PathVariable UUID id, @RequestBody Budget updatedBudget, Authentication authentication) {
        return ResponseEntity.ok(budgetService.updateBudget(id, updatedBudget, authentication.getName()));
    }

    // ✅ Delete Budget
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBudget(@PathVariable UUID id, Authentication authentication) {
        budgetService.deleteBudget(id, authentication.getName());
        return ResponseEntity.ok("Budget deleted successfully");
    }
    @GetMapping("/warnings")
    public ResponseEntity<List<String>> getBudgetWarnings(Authentication authentication) {
        return ResponseEntity.ok(budgetService.checkBudgetWarnings(authentication.getName()));
    }

}

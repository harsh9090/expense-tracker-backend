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
    public ResponseEntity<Income> addIncome(@RequestBody Income income, Authentication authentication) {
        System.out.println("✅ ADD INCOME API CALLED");
        return ResponseEntity.ok(incomeService.addIncome(income, authentication.getName()));
    }

    // ✅ Get All Income for Logged-in User
    @GetMapping
    public ResponseEntity<List<Income>> getIncome(Authentication authentication) {
        System.out.println("✅ FETCH INCOME API CALLED");
        return ResponseEntity.ok(incomeService.getIncome(authentication.getName()));
    }

    // ✅ Update Income
    @PutMapping("/{id}")
    public ResponseEntity<Income> updateIncome(@PathVariable UUID id, @RequestBody Income income, Authentication authentication) {
        System.out.println("✅ UPDATE INCOME API CALLED");
        return ResponseEntity.ok(incomeService.updateIncome(id, income, authentication.getName()));
    }

    // ✅ Delete Income
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteIncome(@PathVariable UUID id, Authentication authentication) {
        System.out.println("✅ DELETE INCOME API CALLED");
        incomeService.deleteIncome(id, authentication.getName());
        String s = "Income deleted successfully";
        return ResponseEntity.ok(Collections.singletonMap("message", "Income deleted successfully"));
    }
    @GetMapping("/source/{category}")
    public Optional<Object[]> getExpensesByCategory(@PathVariable String category, Authentication authentication) {
        User user = userRepo.findByEmail(authentication.getName())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        System.out.println(user.getEmail());
        return incomeRepo.findByUserAndSource(user, category);
    }
}

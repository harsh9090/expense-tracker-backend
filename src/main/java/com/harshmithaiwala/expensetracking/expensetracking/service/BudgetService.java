package com.harshmithaiwala.expensetracking.expensetracking.service;

import com.harshmithaiwala.expensetracking.expensetracking.model.Budget;
import com.harshmithaiwala.expensetracking.expensetracking.model.Expense;
import com.harshmithaiwala.expensetracking.expensetracking.model.User;
import com.harshmithaiwala.expensetracking.expensetracking.repo.BudgetRepo;
import com.harshmithaiwala.expensetracking.expensetracking.repo.ExpenseRepo;
import com.harshmithaiwala.expensetracking.expensetracking.repo.UserRepo;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BudgetService {

    private final BudgetRepo budgetRepo;
    private final UserRepo userRepo;
    private final ExpenseRepo expenseRepo;

    public BudgetService(BudgetRepo budgetRepo, UserRepo userRepo, ExpenseRepo expenseRepo) {
        this.budgetRepo = budgetRepo;
        this.userRepo = userRepo;
        this.expenseRepo = expenseRepo;
    }

    // ✅ Add or Update Budget
    public Budget saveBudget(Budget budget, String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        // Check if a budget for this category, month, and year already exists
        Budget existingBudget = budgetRepo.findByUserAndCategoryAndMonthAndYear(user, budget.getCategory(), budget.getMonth(), budget.getYear());

        if (existingBudget != null) {
            existingBudget.setAmount(budget.getAmount()); // Update existing budget amount
            return budgetRepo.save(existingBudget);
        }

        // Create new budget
        budget.setUser(user);
        return budgetRepo.save(budget);
    }

    // ✅ Get All Budgets for Logged-in User
    public List<Budget> getBudgets(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        return budgetRepo.findByUser(user);
    }

    // ✅ Delete a Budget
    public void deleteBudget(UUID id, String email) {
        Budget budget = budgetRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Budget not found"));

        if (!budget.getUser().getEmail().equals(email)) {
            throw new RuntimeException("Unauthorized to delete this budget");
        }

        budgetRepo.delete(budget);
    }
    public Budget updateBudget(UUID id, Budget updatedBudget, String email) {
        Budget existingBudget = budgetRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Budget not found"));

        if (!existingBudget.getUser().getEmail().equals(email)) {
            throw new RuntimeException("Unauthorized to update this budget");
        }

        existingBudget.setCategory(updatedBudget.getCategory());
        existingBudget.setAmount(updatedBudget.getAmount());
        existingBudget.setMonth(updatedBudget.getMonth());
        existingBudget.setYear(updatedBudget.getYear());

        return budgetRepo.save(existingBudget);
    }
    public List<String> checkBudgetWarnings(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        List<Budget> budgets = budgetRepo.findByUser(user);
        List<Expense> expenses = expenseRepo.findByUser(user);

        List<String> warnings = new ArrayList<>();

        for (Budget budget : budgets) {
            double totalSpent = expenses.stream()
                    .filter(expense -> expense.getCategory().equalsIgnoreCase(budget.getCategory()))
                    .mapToDouble(Expense::getAmount)
                    .sum();

            if (totalSpent > budget.getAmount()) {
                warnings.add("You exceeded the budget for " + budget.getCategory() + ". Spent: ₹" + totalSpent);
            }
        }
        return warnings;
    }

}

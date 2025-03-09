package com.harshmithaiwala.expensetracking.expensetracking.service;

import com.harshmithaiwala.expensetracking.expensetracking.model.*;
import com.harshmithaiwala.expensetracking.expensetracking.repo.BudgetRepo;
import com.harshmithaiwala.expensetracking.expensetracking.repo.ExpenseRepo;
import com.harshmithaiwala.expensetracking.expensetracking.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ExpenseService {

    @Autowired
    private final ExpenseRepo expenseRepository;
    private final UserRepo userRepository;
    @Autowired
    private ExpenseRepo expenseRepo;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private BudgetRepo budgetRepo;
    @Autowired
    private TransactionService transactionService;

    public ExpenseService(ExpenseRepo expenseRepository, UserRepo userRepository) {
        this.expenseRepository = expenseRepository;
        this.userRepository = userRepository;
    }


    // ✅ Add an Expense
    public Map<String, String> addExpense(Expense expense, String email) {
        System.out.println("🟡 Adding expense for user: " + email);

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        expense.setUser(user);
        Expense savedExpense = expenseRepo.save(expense);

        // 🚀 Check if the expense exceeds the budget

        Budget budget = budgetRepo.findByUserAndCategory(user, expense.getCategory());
        String message = "Expense added successfully";

        if (budget != null) {
            double totalSpent = expenseRepo.getTotalSpentByCategory(user, expense.getCategory());
            System.out.println("🔴 Total Spent via Query: " + totalSpent);
            if (totalSpent > budget.getAmount()) {
                message = "⚠ Warning: You exceeded the budget for " + budget.getCategory() + ". Spent: ₹" + totalSpent;
            }
        }

        Map<String, String> response = new HashMap<>();
        response.put("message", message);
        response.put("expenseId", savedExpense.getId().toString());
        return response;
    }



    // ✅ Get All Expenses for Logged-in User
    public List<Expense> getExpenses(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return expenseRepository.findByUser(user);
    }

    // ✅ Update an Expense
    public Expense updateExpense(UUID id, Expense updatedExpense, String email) {
        Expense existingExpense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        if (!existingExpense.getUser().getEmail().equals(email)) {
            throw new RuntimeException("Unauthorized to update this expense");
        }

        existingExpense.setAmount(updatedExpense.getAmount());
        existingExpense.setCategory(updatedExpense.getCategory());
        existingExpense.setDescription(updatedExpense.getDescription());
        existingExpense.setDate(updatedExpense.getDate());

        return expenseRepository.save(existingExpense);
    }

    // ✅ Delete an Expense
    public void deleteExpense(UUID id, String email) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        if (!expense.getUser().getEmail().equals(email)) {
            throw new RuntimeException("Unauthorized to delete this expense");
        }

        expenseRepository.delete(expense);
    }

    // Get Monthly Expenses
    public List<Expense> getMonthlyExpenses(String email, int month, int year) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        
        return expenseRepository.findByUserAndMonthAndYear(user, month, year);
    }
}

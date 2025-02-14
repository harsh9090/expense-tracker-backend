package com.harshmithaiwala.expensetracking.expensetracking.service;

import com.harshmithaiwala.expensetracking.expensetracking.model.Budget;
import com.harshmithaiwala.expensetracking.expensetracking.model.Expense;
import com.harshmithaiwala.expensetracking.expensetracking.model.User;
import com.harshmithaiwala.expensetracking.expensetracking.repo.BudgetRepo;
import com.harshmithaiwala.expensetracking.expensetracking.repo.ExpenseRepo;
import com.harshmithaiwala.expensetracking.expensetracking.repo.UserRepo;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class NotificationService {

    private final BudgetRepo budgetRepo;
    private final ExpenseRepo expenseRepo;
    private final UserRepo userRepo;

    public NotificationService(BudgetRepo budgetRepo, ExpenseRepo expenseRepo, UserRepo userRepo) {
        this.budgetRepo = budgetRepo;
        this.expenseRepo = expenseRepo;
        this.userRepo = userRepo;
    }

    // ✅ Get Budget Exceeding Notifications
    public List<String> getNotifications(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        List<String> notifications = new ArrayList<>();

        List<Budget> budgets = budgetRepo.findByUser(user);
        for (Budget budget : budgets) {
            double totalSpent = expenseRepo.findByUser(user).stream()
                    .filter(e -> e.getCategory().equalsIgnoreCase(budget.getCategory()))
                    .mapToDouble(Expense::getAmount)
                    .sum();

            if (totalSpent > budget.getAmount()) {
                notifications.add("⚠ You have exceeded your budget for " + budget.getCategory());
            }
        }

        return notifications;
    }
}

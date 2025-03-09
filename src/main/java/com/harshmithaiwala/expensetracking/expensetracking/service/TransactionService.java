package com.harshmithaiwala.expensetracking.expensetracking.service;

import com.harshmithaiwala.expensetracking.expensetracking.model.Expense;
import com.harshmithaiwala.expensetracking.expensetracking.model.Income;
import com.harshmithaiwala.expensetracking.expensetracking.model.TransactionType;
import com.harshmithaiwala.expensetracking.expensetracking.repo.ExpenseRepo;
import com.harshmithaiwala.expensetracking.expensetracking.repo.IncomeRepo;
import com.harshmithaiwala.expensetracking.expensetracking.repo.UserRepo;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TransactionService {

    private final ExpenseRepo expenseRepo;
    private final IncomeRepo incomeRepo;
    private final UserRepo userRepo;

    public TransactionService(ExpenseRepo expenseRepo, IncomeRepo incomeRepo, UserRepo userRepo) {
        this.expenseRepo = expenseRepo;
        this.incomeRepo = incomeRepo;
        this.userRepo = userRepo;
    }

    // ✅ Get All Transactions for a User (Merged Expenses & Income)
    public List<Map<String, Object>> getTransactions(String email) {
        var user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        // 🟢 Fetch expenses
        List<Map<String, Object>> transactions = new ArrayList<>();
        List<Expense> expenses = expenseRepo.findByUser(user);

        for (Expense expense : expenses) {
            Map<String, Object> transaction = new HashMap<>();
            transaction.put("id", expense.getId());
            transaction.put("amount", -expense.getAmount()); // Negative for expenses
            transaction.put("category", expense.getCategory());
            transaction.put("type", TransactionType.EXPENSE);
            transaction.put("description", expense.getDescription());
            transaction.put("date", expense.getDate());
            transactions.add(transaction);
        }

        // 🟢 Fetch income
        List<Income> incomes = incomeRepo.findByUser(user);

        for (Income income : incomes) {
            Map<String, Object> transaction = new HashMap<>();
            transaction.put("id", income.getId());
            transaction.put("amount", income.getAmount()); // Positive for income
            transaction.put("category", income.getCategory());
            transaction.put("type", TransactionType.INCOME);
            transaction.put("description", "Income from category: " + income.getCategory());
            transaction.put("date", income.getDate());
            transactions.add(transaction);
        }

        // 🔄 Sort transactions by date (Newest First)
        transactions.sort((t1, t2) -> ((Date) t2.get("date")).compareTo((Date) t1.get("date")));

        return transactions;
    }
}

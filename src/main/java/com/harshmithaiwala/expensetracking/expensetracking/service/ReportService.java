package com.harshmithaiwala.expensetracking.expensetracking.service;

import com.harshmithaiwala.expensetracking.expensetracking.model.Expense;
import com.harshmithaiwala.expensetracking.expensetracking.model.Income;
import com.harshmithaiwala.expensetracking.expensetracking.model.User;
import com.harshmithaiwala.expensetracking.expensetracking.repo.ExpenseRepo;
import com.harshmithaiwala.expensetracking.expensetracking.repo.IncomeRepo;
import com.harshmithaiwala.expensetracking.expensetracking.repo.UserRepo;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportService {

    private final ExpenseRepo expenseRepo;
    private final IncomeRepo incomeRepo;
    private final UserRepo userRepo;

    public ReportService(ExpenseRepo expenseRepo, IncomeRepo incomeRepo, UserRepo userRepo) {
        this.expenseRepo = expenseRepo;
        this.incomeRepo = incomeRepo;
        this.userRepo = userRepo;
    }

    // ✅ Get Monthly Summary (Total Expenses & Income for Each Month)
    public List<Map<String, Object>> getMonthlySummary(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        List<Map<String, Object>> summaryList = new ArrayList<>();

        // 🔹 Group expenses by month
        Map<String, Double> expenseSummary = expenseRepo.findByUser(user)
                .stream()
                .collect(Collectors.groupingBy(
                        e -> e.getDate().toInstant().toString().substring(0, 7), // YYYY-MM
                        Collectors.summingDouble(Expense::getAmount)
                ));

        List<Expense> expenses = expenseRepo.findByUser(user);
        List<Income> incomes = incomeRepo.findByUser(user);
        for(Expense expense : expenses) {
            System.out.println(expense);
        }
        for(Income income: incomes) {
            System.out.println(income);
        }
        // 🔹 Group income by month
        Map<String, Double> incomeSummary = incomeRepo.findByUser(user)
                .stream()
                .collect(Collectors.groupingBy(
                        i -> i.getDate().toInstant().toString().substring(0, 7), // YYYY-MM
                        Collectors.summingDouble(Income::getAmount)
                ));
        System.out.println(incomeSummary);

        // 🔹 Combine the results
        Set<String> months = new HashSet<>();
        months.addAll(expenseSummary.keySet());
        months.addAll(incomeSummary.keySet());

        for (String month : months) {
            System.out.println(month+" "+expenseSummary.get(month)+" "+incomeSummary.get(month) );
            Map<String, Object> summary = new HashMap<>();
            summary.put("month", month);
            summary.put("total_income", incomeSummary.getOrDefault(month, 0.0));
            summary.put("total_expense", expenseSummary.getOrDefault(month, 0.0));
            summaryList.add(summary);
        }

        return summaryList;
    }
    // ✅ Get Category-wise Breakdown of Expenses
    public List<Map<String, Object>> getCategorySummary(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        List<Map<String, Object>> categoryList = new ArrayList<>();

        // 🔹 Group expenses by category
        Map<String, Double> expenseSummary = expenseRepo.findByUser(user)
                .stream()
                .collect(Collectors.groupingBy(
                        Expense::getCategory,
                        Collectors.summingDouble(Expense::getAmount)
                ));

        // 🔹 Convert to a list
        for (Map.Entry<String, Double> entry : expenseSummary.entrySet()) {
            Map<String, Object> category = new HashMap<>();
            category.put("category", entry.getKey());
            category.put("total_spent", entry.getValue());
            categoryList.add(category);
        }
        return categoryList;
    }
    public List<Map<String, Object>> getCategoryIncome(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        List<Map<String, Object>> categoryList = new ArrayList<>();

        // 🔹 Group expenses by source
        Map<String, Double> incomeSummary = incomeRepo.findByUser(user)
                .stream()
                .collect(Collectors.groupingBy(
                        Income::getSource,
                        Collectors.summingDouble(Income::getAmount)
                ));

        // 🔹 Convert to a list
        for (Map.Entry<String, Double> entry : incomeSummary.entrySet()) {
            Map<String, Object> category = new HashMap<>();
            category.put("source", entry.getKey());
            category.put("total_spent", entry.getValue());
            categoryList.add(category);
        }
        return categoryList;
    }

}

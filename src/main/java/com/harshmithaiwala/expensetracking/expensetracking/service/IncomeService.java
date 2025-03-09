package com.harshmithaiwala.expensetracking.expensetracking.service;

import com.harshmithaiwala.expensetracking.expensetracking.model.Income;
import com.harshmithaiwala.expensetracking.expensetracking.model.Transaction;
import com.harshmithaiwala.expensetracking.expensetracking.model.TransactionType;
import com.harshmithaiwala.expensetracking.expensetracking.model.User;
import com.harshmithaiwala.expensetracking.expensetracking.repo.IncomeRepo;
import com.harshmithaiwala.expensetracking.expensetracking.repo.UserRepo;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

@Service
public class IncomeService {

    private final IncomeRepo incomeRepo;
    private final UserRepo userRepo;
    private final TransactionService transactionService;

    public IncomeService(IncomeRepo incomeRepo, UserRepo userRepo, TransactionService transactionService) {
        this.incomeRepo = incomeRepo;
        this.userRepo = userRepo;
        this.transactionService = transactionService;
    }

    // ✅ Add Income
    public Income addIncome(Income income, String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        income.setUser(user);
        Income savedIncome = incomeRepo.save(income);

        return savedIncome;
    }


    // ✅ Get All Income for Logged-in User
    public List<Income> getIncome(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        return incomeRepo.findByUser(user);
    }

    // ✅ Update Income
    public Income updateIncome(UUID id, Income updatedIncome, String email) {
        Income existingIncome = incomeRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Income not found"));

        if (!existingIncome.getUser().getEmail().equals(email)) {
            throw new RuntimeException("Unauthorized to update this income");
        }

        existingIncome.setAmount(updatedIncome.getAmount());
        existingIncome.setSource(updatedIncome.getSource());
        existingIncome.setDate(updatedIncome.getDate());

        return incomeRepo.save(existingIncome);
    }

    // ✅ Delete Income
    public void deleteIncome(UUID id, String email) {
        Income income = incomeRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Income not found"));

        if (!income.getUser().getEmail().equals(email)) {
            throw new RuntimeException("Unauthorized to delete this income");
        }

        incomeRepo.delete(income);
    }

    // Get Monthly Income
    public List<Income> getMonthlyIncome(String email, Integer year, Integer month) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // If year or month not provided, use current month
        if (year == null || month == null) {
            Calendar cal = Calendar.getInstance();
            year = cal.get(Calendar.YEAR);
            month = cal.get(Calendar.MONTH) + 1; // Calendar months are 0-based
        }

        // Create date range for the specified month
        Calendar startCal = Calendar.getInstance();
        startCal.set(year, month - 1, 1, 0, 0, 0); // First day of month
        startCal.set(Calendar.MILLISECOND, 0);
        
        Calendar endCal = Calendar.getInstance();
        endCal.set(year, month - 1, startCal.getActualMaximum(Calendar.DAY_OF_MONTH), 23, 59, 59);
        endCal.set(Calendar.MILLISECOND, 999);

        return incomeRepo.findByUserAndDateBetween(user, startCal.getTime(), endCal.getTime());
    }
}

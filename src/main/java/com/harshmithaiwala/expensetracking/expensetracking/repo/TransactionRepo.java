package com.harshmithaiwala.expensetracking.expensetracking.repo;

import com.harshmithaiwala.expensetracking.expensetracking.model.Transaction;
import com.harshmithaiwala.expensetracking.expensetracking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepo extends JpaRepository<Transaction, UUID> {

    // ✅ Fetch transactions for a specific user
    List<Transaction> findByUser(User user);

    // ✅ Fetch transactions by type (INCOME or EXPENSE)
    List<Transaction> findByUserAndType(User user, com.harshmithaiwala.expensetracking.expensetracking.model.TransactionType type);

    // ✅ Fetch transactions within a date range
    List<Transaction> findByUserAndDateBetween(User user, java.util.Date startDate, java.util.Date endDate);
}

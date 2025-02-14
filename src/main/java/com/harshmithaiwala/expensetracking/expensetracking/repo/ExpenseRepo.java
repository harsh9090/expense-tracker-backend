package com.harshmithaiwala.expensetracking.expensetracking.repo;

import com.harshmithaiwala.expensetracking.expensetracking.model.Expense;
import com.harshmithaiwala.expensetracking.expensetracking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ExpenseRepo extends JpaRepository<Expense, UUID> {

    // ✅ Fetch all expenses for a specific user
    List<Expense> findByUser(User user);

    Optional<Object[]> findByUserAndCategory(User user, String category);
    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.user = :user AND e.category = :category")
    Double getTotalSpentByCategory(@Param("user") User user, @Param("category") String category);



}

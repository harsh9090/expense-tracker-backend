package com.harshmithaiwala.expensetracking.expensetracking.repo;

import com.harshmithaiwala.expensetracking.expensetracking.model.Budget;
import com.harshmithaiwala.expensetracking.expensetracking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface BudgetRepo extends JpaRepository<Budget, UUID> {

    // ✅ Fetch all budgets for a specific user
    List<Budget> findByUser(User user);

    // ✅ Fetch budget for a specific category in a given month & year
    Budget findByUserAndCategoryAndMonthAndYear(User user, String category, Integer month, Integer year);

    Budget findByUserAndCategory(User user, String category);
}

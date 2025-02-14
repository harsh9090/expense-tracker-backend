package com.harshmithaiwala.expensetracking.expensetracking.repo;

import com.harshmithaiwala.expensetracking.expensetracking.model.Income;
import com.harshmithaiwala.expensetracking.expensetracking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IncomeRepo extends JpaRepository<Income, UUID> {

    // ✅ Fetch all income records for a specific user
    List<Income> findByUser(User user);
    Optional<Object[]> findByUserAndSource(User user, String source);
    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Income e WHERE e.user = :user AND e.source = :source")
    Double getTotalSpentByCategory(@Param("user") User user, @Param("source") String source);

}

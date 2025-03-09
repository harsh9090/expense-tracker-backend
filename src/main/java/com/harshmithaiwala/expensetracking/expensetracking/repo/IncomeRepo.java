package com.harshmithaiwala.expensetracking.expensetracking.repo;

import com.harshmithaiwala.expensetracking.expensetracking.model.Income;
import com.harshmithaiwala.expensetracking.expensetracking.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface IncomeRepo extends JpaRepository<Income, UUID> {

    // ✅ Fetch all income records for a specific user
    List<Income> findByUser(User user);

    // Find by source
    Optional<Object[]> findByUserAndSource(User user, String source);

    // Find by category
    Optional<Object[]> findByUserAndCategory(User user, String category);

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Income e WHERE e.user = :user AND e.source = :source")
    Double getTotalBySource(@Param("user") User user, @Param("source") String source);

    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Income e WHERE e.user = :user AND e.category = :category")
    Double getTotalByCategory(@Param("user") User user, @Param("category") String category);

    // Find income records between two dates for a specific user
    List<Income> findByUserAndDateBetween(User user, Date startDate, Date endDate);

    @Query("SELECT e FROM Income e WHERE e.user = :user AND MONTH(e.date) = :month AND YEAR(e.date) = :year")
    List<Income> findByUserAndMonthAndYear(@Param("user") User user, @Param("month") int month, @Param("year") int year);
}

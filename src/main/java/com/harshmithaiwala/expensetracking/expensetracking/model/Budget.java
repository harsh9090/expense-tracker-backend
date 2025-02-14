package com.harshmithaiwala.expensetracking.expensetracking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.UUID;

@Entity
@Table(name = "budgets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Budget {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne  // ✅ Establishes relationship with User
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String category;  // Example: Food, Transport, Shopping
    private Double amount;  // Budget amount limit

    private Integer month;  // Budget applies to a specific month (1-12)
    private Integer year;   // Budget applies to a specific year
}

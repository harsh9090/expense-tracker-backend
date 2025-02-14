package com.harshmithaiwala.expensetracking.expensetracking.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private Double amount;  // Positive for income, negative for expenses

    private String category;  // e.g., Salary, Food, Transport

    @Enumerated(EnumType.STRING)
    private TransactionType type;  // INCOME or EXPENSE

    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;
}

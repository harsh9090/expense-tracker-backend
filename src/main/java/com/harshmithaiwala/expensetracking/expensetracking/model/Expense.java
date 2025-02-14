package com.harshmithaiwala.expensetracking.expensetracking.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "expenses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY) // ✅ Establish relationship with User
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private Double amount;
    private String category;
    private String description;

    @Temporal(TemporalType.TIMESTAMP)
    private Date date;

    public Double getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "Expense{" +
                "id=" + id +
                ", amount=" + amount +
                ", category='" + category + '\'' +
                ", description='" + description + '\'' +
                ", date=" + date +
                '}';
    }
}

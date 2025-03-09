package com.harshmithaiwala.expensetracking.expensetracking.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.UUID;

@Entity
@Table(name = "income")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Income {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private Double amount;
    private String category;
    private String source;  // e.g., Salary, Freelance, Investments
    private java.util.Date date;

    @Override
    public String toString() {
        return "Income{" +
                "id=" + id +
                ", user=" + user +
                ", amount=" + amount +
                ", category='" + category + '\'' +
                ", source='" + source + '\'' +
                ", date=" + date +
                '}';
    }
}

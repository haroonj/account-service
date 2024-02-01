package com.digitinary.accountservice.entity;

import com.digitinary.accountservice.model.AccountType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {
    @Id
    private Long id;
    private Long customerId;
    private Double balance;
    private String status;
    @Enumerated(EnumType.STRING)
    private AccountType type;
}

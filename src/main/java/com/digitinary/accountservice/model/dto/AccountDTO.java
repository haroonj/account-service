package com.digitinary.accountservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTO {
    private Long id;
    private Long customerId;
    private Double balance;
    private String status;
    private String type;
}
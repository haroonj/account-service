package com.digitinary.accountservice.event.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerEvent implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    private Long customerId;
    private String name;
    private String legalId;
    private String type;
    private String address;
}

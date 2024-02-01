package com.digitinary.accountservice.repository;

import com.digitinary.accountservice.entity.Account;
import com.digitinary.accountservice.model.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Long countByCustomerId(Long customerId);

    Long countByCustomerIdAndType(Long customerId, AccountType type);
}

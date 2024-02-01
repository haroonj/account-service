package com.digitinary.accountservice.model.mapper;

import com.digitinary.accountservice.entity.Account;
import com.digitinary.accountservice.model.AccountType;
import com.digitinary.accountservice.model.dto.AccountDTO;

public class AccountMapper {

    public static Account toEntity(AccountDTO accountDTO) {
        Account account = new Account();
        account.setId(accountDTO.getId());
        account.setCustomerId(accountDTO.getCustomerId());
        account.setBalance(accountDTO.getBalance());
        account.setStatus(accountDTO.getStatus());
        account.setType(AccountType.valueOf(accountDTO.getType().toUpperCase()));
        return account;
    }

    public static AccountDTO toDTO(Account account) {
        AccountDTO accountDTO = new AccountDTO();
        accountDTO.setId(account.getId());
        accountDTO.setCustomerId(account.getCustomerId());
        accountDTO.setBalance(account.getBalance());
        accountDTO.setStatus(account.getStatus());
        accountDTO.setType(account.getType().name());
        return accountDTO;
    }
}

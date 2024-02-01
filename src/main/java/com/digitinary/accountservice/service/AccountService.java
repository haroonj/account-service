package com.digitinary.accountservice.service;

import com.digitinary.accountservice.entity.Account;
import com.digitinary.accountservice.exception.AccountNotFoundException;
import com.digitinary.accountservice.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Slf4j
@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    public Optional<Account> getAccountById(Long id) {
        return accountRepository.findById(id);
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public Account updateAccount(Long id, Account accountDetails) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException("Account not found for this id :: " + id));
        log.info(String.valueOf(account.getId()));
        account.setBalance(accountDetails.getBalance());
        account.setStatus(accountDetails.getStatus());

        return accountRepository.save(account);
    }

    public void deleteAccount(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException("Account not found for this id :: " + id));
        accountRepository.delete(account);
    }
}

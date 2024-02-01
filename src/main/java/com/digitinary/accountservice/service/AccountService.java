package com.digitinary.accountservice.service;

import com.digitinary.accountservice.entity.Account;
import com.digitinary.accountservice.exception.AccountNotFoundException;
import com.digitinary.accountservice.exception.InvalidAccountIdException;
import com.digitinary.accountservice.exception.InvalidAccountTypeException;
import com.digitinary.accountservice.exception.MaxAccountsReachedException;
import com.digitinary.accountservice.model.AccountType;
import com.digitinary.accountservice.model.dto.AccountDTO;
import com.digitinary.accountservice.model.mapper.AccountMapper;
import com.digitinary.accountservice.repository.AccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public AccountDTO createAccount(AccountDTO account) {
        validateAccountId(account.getId(), account.getCustomerId());
        validateAccountLimitForCustomer(account.getCustomerId());
        validateSalaryAccountType(account.getCustomerId(), account.getType());
        isValidAccountType(account.getType());

        return AccountMapper.toDTO(
                accountRepository.save(
                        AccountMapper.toEntity(account
                        )));
    }

    public AccountDTO getAccountById(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(id));
        return AccountMapper.toDTO(account);
    }

    public List<AccountDTO> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream()
                .map(AccountMapper::toDTO)
                .toList();
    }

    public AccountDTO updateAccount(Long id, AccountDTO accountDetails) {
        validateSalaryAccountType(accountDetails.getCustomerId(), accountDetails.getType());
        isValidAccountType(accountDetails.getType());
        Account account = accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(id));
        account.setBalance(accountDetails.getBalance());
        account.setStatus(accountDetails.getStatus());
        account.setType(AccountType.valueOf(accountDetails.getType().toUpperCase()));

        return AccountMapper.toDTO(accountRepository.save(account));
    }

    public void deleteAccount(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(id));
        accountRepository.delete(account);
    }

    private void validateAccountId(Long accountId, Long customerId) {
        String accountIdStr = Long.toString(accountId);
        String customerIdStr = Long.toString(customerId);

        if (!accountIdStr.matches("\\d{10}")) {
            throw new InvalidAccountIdException("Account ID must be 10 digits.");
        }

        if (!accountIdStr.startsWith(customerIdStr)) {
            throw new InvalidAccountIdException("The first 7 digits of the Account ID must match the Customer ID.");
        }
    }

    private void validateAccountLimitForCustomer(Long customerId) {
        long existingAccountsCount = accountRepository.countByCustomerId(customerId);
        if (existingAccountsCount >= 10) {
            throw new MaxAccountsReachedException("A customer can have up to 10 accounts.");
        }
    }

    private void validateSalaryAccountType(Long customerId, String sAccountType) {
        AccountType accountType = AccountType.valueOf(sAccountType.toUpperCase());
        if (accountType.equals(AccountType.SALARY)) {
            long salaryAccountsCount = accountRepository.countByCustomerIdAndType(customerId, AccountType.SALARY);
            if (salaryAccountsCount > 0) {
                throw new InvalidAccountTypeException("Only one salary account is allowed per customer.");
            }
        }
    }

    public void isValidAccountType(String sAccountType) {
        try {
            Enum.valueOf(AccountType.class, sAccountType);
        } catch (IllegalArgumentException e) {
            throw new InvalidAccountTypeException("Invalid Account Type");
        }
    }

}

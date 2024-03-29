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
import java.util.Random;

@Slf4j
@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final Random random;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
        this.random = new Random();
    }

    @Transactional
    public AccountDTO createAccount(AccountDTO account) {
        validateAccountId(account.getId(), account.getCustomerId());
        validateAccountLimitForCustomer(account.getCustomerId());
        validateSalaryAccountType(account.getCustomerId(), account.getType());
        isValidAccountType(account.getType());

        Account savedAccount = accountRepository.save(AccountMapper.toEntity(account));
        log.debug("Account created with values {}", savedAccount);
        return AccountMapper.toDTO(savedAccount);
    }

    public AccountDTO getAccountById(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(id));
        return AccountMapper.toDTO(account);
    }

    public List<AccountDTO> getAllAccounts() {
        List<Account> accounts = accountRepository.findAll();
        return accounts.stream().map(AccountMapper::toDTO).toList();
    }
    @Transactional
    public AccountDTO updateAccount(Long id, AccountDTO accountDetails) {
        validateSalaryAccountType(accountDetails.getCustomerId(), accountDetails.getType());
        isValidAccountType(accountDetails.getType());
        Account account = accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(id));
        account.setBalance(accountDetails.getBalance());
        account.setStatus(accountDetails.getStatus());
        account.setType(AccountType.valueOf(accountDetails.getType().toUpperCase()));

        Account savedAccount = accountRepository.save(account);
        log.debug("Account updated with values {}", savedAccount);
        return AccountMapper.toDTO(account);
    }
    @Transactional
    public void deleteAccount(Long id) {
        Account account = accountRepository.findById(id).orElseThrow(() -> new AccountNotFoundException(id));
        accountRepository.delete(account);
    }
    @Transactional
    public void createDefaultAccount(Long customerId) {
        Account account = new Account();
        account.setId(generateRandomAccountId(customerId));
        account.setCustomerId(customerId);
        account.setBalance(0.0);
        account.setStatus("INACTIVE");
        account.setType(AccountType.SAVINGS);

        accountRepository.save(account);
        log.debug("Default account created with values {}", account);
    }

    @Transactional
    public void deleteAllByCustomerId(Long customerId){
        accountRepository.deleteAllByCustomerId(customerId);
        log.debug("All accounts have been deleted for customer id {}", customerId);
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

    private Long generateRandomAccountId(Long customerId) {
        return Long.parseLong(customerId + String.valueOf(100 + random.nextInt(900)));
    }
}

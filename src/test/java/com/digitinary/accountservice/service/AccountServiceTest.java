package com.digitinary.accountservice.service;

import com.digitinary.accountservice.entity.Account;
import com.digitinary.accountservice.exception.AccountNotFoundException;
import com.digitinary.accountservice.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createAccount() {

        Account account = new Account();
        when(accountRepository.save(account)).thenReturn(account);

        Account createdAccount = accountService.createAccount(account);

        assertNotNull(createdAccount);
        assertEquals(account, createdAccount);
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    void getAccountById_existingId_shouldReturnAccount() {

        Long accountId = 1L;
        Account account = new Account();
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        Optional<Account> retrievedAccount = accountService.getAccountById(accountId);

        assertTrue(retrievedAccount.isPresent());
        assertEquals(account, retrievedAccount.get());
        verify(accountRepository, times(1)).findById(accountId);
    }

    @Test
    void getAccountById_nonExistingId_shouldReturnEmptyOptional() {

        Long accountId = 1L;
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        Optional<Account> retrievedAccount = accountService.getAccountById(accountId);

        assertFalse(retrievedAccount.isPresent());
        verify(accountRepository, times(1)).findById(accountId);
    }

    @Test
    void getAllAccounts() {

        List<Account> accounts = new ArrayList<>();
        when(accountRepository.findAll()).thenReturn(accounts);

        List<Account> retrievedAccounts = accountService.getAllAccounts();

        assertEquals(accounts, retrievedAccounts);
        verify(accountRepository, times(1)).findAll();
    }

    @Test
    void updateAccount_existingId_shouldReturnUpdatedAccount() {

        Long accountId = 1L;
        Account existingAccount = new Account();
        existingAccount.setId(accountId);
        Account updatedAccount = new Account();
        updatedAccount.setId(accountId);
        updatedAccount.setBalance(1000.0);
        updatedAccount.setStatus("ACTIVE");

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(existingAccount));
        when(accountRepository.save(existingAccount)).thenReturn(updatedAccount);

        Account result = accountService.updateAccount(accountId, updatedAccount);

        assertNotNull(result);
        assertEquals(updatedAccount, result);
        verify(accountRepository, times(1)).findById(accountId);
        verify(accountRepository, times(1)).save(existingAccount);
    }

    @Test
    void updateAccount_nonExistingId_shouldThrowAccountNotFoundException() {

        Long accountId = 1L;
        Account updatedAccount = new Account();
        updatedAccount.setId(accountId);
        updatedAccount.setBalance(1000.0);
        updatedAccount.setStatus("ACTIVE");

        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.updateAccount(accountId, updatedAccount));
        verify(accountRepository, times(1)).findById(accountId);
        verify(accountRepository, never()).save(any(Account.class));
    }

    @Test
    void deleteAccount_existingId_shouldDeleteAccount() {

        Long accountId = 1L;
        Account account = new Account();
        account.setId(accountId);

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        accountService.deleteAccount(accountId);

        verify(accountRepository, times(1)).findById(accountId);
        verify(accountRepository, times(1)).delete(account);
    }

    @Test
    void deleteAccount_nonExistingId_shouldThrowAccountNotFoundException() {

        Long accountId = 1L;

        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.deleteAccount(accountId));
        verify(accountRepository, times(1)).findById(accountId);
        verify(accountRepository, never()).delete(any(Account.class));
    }
}
package com.digitinary.accountservice.service;

import com.digitinary.accountservice.entity.Account;
import com.digitinary.accountservice.exception.AccountNotFoundException;
import com.digitinary.accountservice.model.AccountType;
import com.digitinary.accountservice.model.dto.AccountDTO;
import com.digitinary.accountservice.model.mapper.AccountMapper;
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
        AccountDTO accountDTO = new AccountDTO(1234567890L, 1234567L, 1000.0, "ACTIVE", "SAVINGS");
        Account account = new Account(1234567890L, 1234567L, 1000.0, "ACTIVE", AccountType.SAVINGS);

        when(accountRepository.save(any(Account.class))).thenReturn(account);

        AccountDTO createdAccountDTO = accountService.createAccount(accountDTO);

        assertNotNull(createdAccountDTO);
        assertEquals(accountDTO.getId(), createdAccountDTO.getId());
        verify(accountRepository, times(1)).save(account);
    }

    @Test
    void getAccountById_existingId_shouldReturnAccountDTO() {
        Long accountId = 1234567890L;
        Account account = new Account(1234567890L, 1234567L, 1000.0, "ACTIVE", AccountType.SAVINGS);
        AccountDTO accountDTO = new AccountDTO(1234567890L, 1234567L, 1000.0, "ACTIVE", "SAVINGS");

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));

        AccountDTO retrievedAccountDTO = accountService.getAccountById(accountId);

        assertEquals(accountDTO, retrievedAccountDTO);
        verify(accountRepository, times(1)).findById(accountId);
    }

    @Test
    void getAccountById_nonExistingId_shouldReturnEmptyOptional() {

        Long accountId = 1L;
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.getAccountById(accountId));
        verify(accountRepository, times(1)).findById(accountId);
    }

    @Test
    void getAllAccounts() {

        List<Account> accounts = new ArrayList<>();
        when(accountRepository.findAll()).thenReturn(accounts);

        List<AccountDTO> retrievedAccounts = accountService.getAllAccounts();

        assertEquals(accounts, retrievedAccounts.stream().map(AccountMapper::toEntity).toList());
        verify(accountRepository, times(1)).findAll();
    }

    @Test
    void updateAccount_existingId_shouldReturnUpdatedAccountDTO() {
        Long accountId = 1234567890L;
        Account existingAccount = new Account(1234567890L, 1234567L, 1000.0, "ACTIVE", AccountType.INVESTMENT);
        AccountDTO updatedAccountDTO = new AccountDTO(1234567890L, 1234567L, 500.0, "ACTIVE", "SAVINGS");
        Account updatedAccount = new Account(1234567890L, 1234567L, 500.0, "ACTIVE", AccountType.SAVINGS);

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(existingAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(updatedAccount);

        AccountDTO resultDTO = accountService.updateAccount(accountId, updatedAccountDTO);

        assertNotNull(resultDTO);
        assertEquals(updatedAccountDTO.getBalance(), resultDTO.getBalance());
        verify(accountRepository, times(1)).findById(accountId);
        verify(accountRepository, times(1)).save(any(Account.class));
    }


    @Test
    void updateAccount_nonExistingId_shouldThrowAccountNotFoundException() {
        Long accountId = 1L;
        AccountDTO updatedAccountDTO = new AccountDTO(1234567890L, 1234567L, 1000.0, "ACTIVE", "SAVINGS");

        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.updateAccount(accountId, updatedAccountDTO));
        verify(accountRepository, times(1)).findById(accountId);
        verify(accountRepository, never()).save(any(Account.class));
    }


    @Test
    void deleteAccount_existingId_shouldDeleteAccount() {
        Long accountId = 1234567890L;
        Account account = new Account();

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(account));
        doNothing().when(accountRepository).delete(account);

        accountService.deleteAccount(accountId);

        verify(accountRepository, times(1)).findById(accountId);
        verify(accountRepository, times(1)).delete(account);
    }


    @Test
    void deleteAccount_nonExistingId_shouldThrowAccountNotFoundException() {
        Long accountId = 1234561234L;

        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        assertThrows(AccountNotFoundException.class, () -> accountService.deleteAccount(accountId));
        verify(accountRepository, times(1)).findById(accountId);
        verify(accountRepository, never()).delete(any(Account.class));
    }

}
package com.digitinary.accountservice.controller;

import com.digitinary.accountservice.entity.Account;
import com.digitinary.accountservice.service.AccountService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AccountController.class)
class AccountControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void whenCreateAccount_thenStatusOkAndAccountCreated() throws Exception {

        Account account = new Account();
        account.setId(1L);
        account.setCustomerId(123L);
        account.setBalance(1000.0);
        account.setStatus("ACTIVE");
        when(accountService.createAccount(any(Account.class))).thenReturn(account);

        mockMvc.perform(post("/accounts").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(account))).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1L)).andExpect(jsonPath("$.customerId").value(123L)).andExpect(jsonPath("$.balance").value(1000.0)).andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void whenGetExistingAccountById_thenStatusOkAndAccountReturned() throws Exception {

        Account account = new Account();
        account.setId(1L);
        account.setCustomerId(123L);
        account.setBalance(1000.0);
        account.setStatus("ACTIVE");
        when(accountService.getAccountById(1L)).thenReturn(Optional.of(account));


        mockMvc.perform(get("/accounts/1")).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1L)).andExpect(jsonPath("$.customerId").value(123L)).andExpect(jsonPath("$.balance").value(1000.0)).andExpect(jsonPath("$.status").value("ACTIVE"));
    }

    @Test
    void whenGetNonExistingAccountById_thenStatusNotFound() throws Exception {

        when(accountService.getAccountById(2L)).thenReturn(Optional.empty());


        mockMvc.perform(get("/accounts/2")).andExpect(status().isNotFound());
    }

    @Test
    void whenGetAllAccounts_thenStatusOkAndAccountsReturned() throws Exception {

        Account account1 = new Account();
        account1.setId(1L);
        Account account2 = new Account();
        account2.setId(2L);
        when(accountService.getAllAccounts()).thenReturn(Arrays.asList(account1, account2));


        mockMvc.perform(get("/accounts")).andExpect(status().isOk()).andExpect(jsonPath("$.length()").value(2)).andExpect(jsonPath("$[0].id").value(1L)).andExpect(jsonPath("$[1].id").value(2L));
    }

    @Test
    void whenUpdateExistingAccount_thenStatusOkAndAccountUpdated() throws Exception {

        Account account = new Account();
        account.setId(1L);
        account.setCustomerId(123L);
        account.setBalance(1500.0);
        account.setStatus("INACTIVE");
        when(accountService.updateAccount(eq(1L), any(Account.class))).thenReturn(account);


        mockMvc.perform(put("/accounts/1").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(account))).andExpect(status().isOk()).andExpect(jsonPath("$.id").value(1L)).andExpect(jsonPath("$.customerId").value(123L)).andExpect(jsonPath("$.balance").value(1500.0)).andExpect(jsonPath("$.status").value("INACTIVE"));
    }

    @Test
    void whenDeleteExistingAccount_thenStatusOk() throws Exception {

        doNothing().when(accountService).deleteAccount(1L);


        mockMvc.perform(delete("/accounts/1")).andExpect(status().isOk());
    }
}

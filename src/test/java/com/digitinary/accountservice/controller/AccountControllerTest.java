package com.digitinary.accountservice.controller;

import com.digitinary.accountservice.entity.Account;
import com.digitinary.accountservice.exception.AccountNotFoundException;
import com.digitinary.accountservice.model.dto.AccountDTO;
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

import static org.mockito.BDDMockito.given;
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
        AccountDTO accountDTO = new AccountDTO(1234567890L, 1234567L, 1000.0, "ACTIVE", "SAVINGS");

        when(accountService.createAccount(any(AccountDTO.class))).thenReturn(accountDTO);

        mockMvc.perform(post("/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(accountDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1234567890L))
                .andExpect(jsonPath("$.customerId").value(1234567L))
                .andExpect(jsonPath("$.balance").value(1000.0))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.type").value("SAVINGS"));
    }

    @Test
    void whenGetExistingAccountById_thenStatusOkAndAccountReturned() throws Exception {

        AccountDTO account = new AccountDTO();
        account.setId(1234567890L);
        account.setCustomerId(1234567L);
        account.setBalance(1000.0);
        account.setStatus("ACTIVE");
        account.setType("INVESTMENT");
        when(accountService.getAccountById(1234567890L)).thenReturn(account);


        mockMvc.perform(get("/accounts/1234567890"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1234567890L))
                .andExpect(jsonPath("$.customerId").value(1234567L))
                .andExpect(jsonPath("$.balance").value(1000.0))
                .andExpect(jsonPath("$.status").value("ACTIVE"))
                .andExpect(jsonPath("$.type").value("INVESTMENT"));
    }

    @Test
    void whenGetNonExistingAccountById_thenStatusNotFound() throws Exception {
        Long accountId = 2482378964L;
        given(accountService.getAccountById(accountId)).willThrow(new AccountNotFoundException(accountId));

        mockMvc.perform(get("/accounts/" + accountId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void whenGetAllAccounts_thenStatusOkAndAccountsReturned() throws Exception {

        AccountDTO account1 = new AccountDTO();
        account1.setId(1234567890L);
        AccountDTO account2 = new AccountDTO();
        account2.setId(2123456789L);
        when(accountService.getAllAccounts()).thenReturn(Arrays.asList(account1, account2));


        mockMvc.perform(get("/accounts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1234567890L))
                .andExpect(jsonPath("$[1].id").value(2123456789L));
    }

    @Test
    void whenUpdateExistingAccount_thenStatusOkAndAccountUpdated() throws Exception {

        AccountDTO account = new AccountDTO();
        account.setId(1234567890L);
        account.setCustomerId(1234567L);
        account.setBalance(1500.0);
        account.setStatus("INACTIVE");
        account.setType("INVESTMENT");
        when(accountService.updateAccount(eq(1234567890L), any(AccountDTO.class))).thenReturn(account);


        mockMvc.perform(put("/accounts/1234567890").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(account)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1234567890L))
                .andExpect(jsonPath("$.customerId").value(1234567L))
                .andExpect(jsonPath("$.balance").value(1500.0))
                .andExpect(jsonPath("$.status").value("INACTIVE"))
                .andExpect(jsonPath("$.type").value("INVESTMENT"));
    }

    @Test
    void whenDeleteExistingAccount_thenStatusOk() throws Exception {

        doNothing().when(accountService).deleteAccount(1L);


        mockMvc.perform(delete("/accounts/1")).andExpect(status().isOk());
    }
}

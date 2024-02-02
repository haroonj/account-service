package com.digitinary.accountservice.controller;

import com.digitinary.accountservice.model.dto.AccountDTO;
import com.digitinary.accountservice.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping
    public ResponseEntity<AccountDTO> createAccount(@RequestBody AccountDTO account) {
        log.debug("creating account request with values {}", account);
        return ResponseEntity.ok(accountService.createAccount(account));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountDTO> getAccountById(@PathVariable Long id) {
        log.debug("get account request with id {}", id);
        return ResponseEntity.ok(accountService.getAccountById(id));
    }

    @GetMapping
    public List<AccountDTO> getAllAccounts() {
        log.debug("get all account request");
        return accountService.getAllAccounts();
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountDTO> updateAccount(@PathVariable Long id, @RequestBody AccountDTO account) {
        log.debug("update account request with id {}", id);
        return ResponseEntity.ok(accountService.updateAccount(id, account));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        log.debug("delete account request with id {}", id);
        accountService.deleteAccount(id);
        return ResponseEntity.ok().build();
    }
}

package com.digitinary.accountservice.repository;

import com.digitinary.accountservice.entity.Account;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class AccountRepositoryTest {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void testSaveAccount() {

        Account account = new Account();
        account.setCustomerId(1L);
        account.setBalance(100.0);
        account.setStatus("ACTIVE");

        Account savedAccount = accountRepository.save(account);

        Assertions.assertNotNull(savedAccount.getId());
        Assertions.assertEquals(account.getCustomerId(), savedAccount.getCustomerId());
        Assertions.assertEquals(account.getBalance(), savedAccount.getBalance());
        Assertions.assertEquals(account.getStatus(), savedAccount.getStatus());
    }

    @Test
    void testFindAccountById() {

        Account account = new Account();
        account.setCustomerId(1L);
        account.setBalance(100.0);
        account.setStatus("ACTIVE");

        Account savedAccount = entityManager.persistAndFlush(account);

        Account foundAccount = accountRepository.findById(savedAccount.getId()).orElse(null);

        Assertions.assertNotNull(foundAccount);
        Assertions.assertEquals(savedAccount.getId(), foundAccount.getId());
        Assertions.assertEquals(savedAccount.getCustomerId(), foundAccount.getCustomerId());
        Assertions.assertEquals(savedAccount.getBalance(), foundAccount.getBalance());
        Assertions.assertEquals(savedAccount.getStatus(), foundAccount.getStatus());
    }

}
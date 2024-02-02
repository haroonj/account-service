package com.digitinary.accountservice.event.handler;

import com.digitinary.accountservice.event.model.CustomerEvent;
import com.digitinary.accountservice.service.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CustomerEventHandler {

    private final AccountService accountService;

    public CustomerEventHandler(AccountService accountService) {
        this.accountService = accountService;
    }

    @RabbitListener(queues = "#{@customerCreatedQueue}")
    public void handleCustomerCreated(CustomerEvent event) {
        log.debug("customer created event with values {}", event.toString());
        accountService.createDefaultAccount(event.getCustomerId());
    }

    @RabbitListener(queues = "#{@customerDeletedQueue}")
    public void handleCustomerDeleted(CustomerEvent event) {
        log.debug("customer deleted event with values {}", event.toString());
        accountService.deleteAllByCustomerId(event.getCustomerId());
    }
}

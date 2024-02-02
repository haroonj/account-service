package com.digitinary.accountservice.event.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.queues.created}")
    private String customerCreatedQueueName;

    @Value("${rabbitmq.queues.deleted}")
    private String customerDeletedQueueName;

    @Value("${rabbitmq.exchange}")
    private String directExchangeName;

    @Value("${rabbitmq.routingKeys.created}")
    private String createdRoutingKey;

    @Value("${rabbitmq.routingKeys.deleted}")
    private String deletedRoutingKey;

    @Bean
    public Queue customerCreatedQueue() {
        return new Queue(customerCreatedQueueName, true);
    }

    @Bean
    public Queue customerDeletedQueue() {
        return new Queue(customerDeletedQueueName, true);
    }

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(directExchangeName);
    }

    @Bean
    public Binding bindingCreated(DirectExchange directExchange, Queue customerCreatedQueue) {
        return BindingBuilder.bind(customerCreatedQueue).to(directExchange).with(createdRoutingKey);
    }

    @Bean
    public Binding bindingDeleted(DirectExchange directExchange, Queue customerDeletedQueue) {
        return BindingBuilder.bind(customerDeletedQueue).to(directExchange).with(deletedRoutingKey);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}

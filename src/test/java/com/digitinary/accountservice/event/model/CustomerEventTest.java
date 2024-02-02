package com.digitinary.accountservice.event.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.util.SerializationUtils;

class CustomerEventTest {

    @Test
    void testGettersAndSetters() {
        // Arrange
        Long customerId = 1L;
        String name = "John Doe";
        String legalId = "123456789";
        String type = "Individual";
        String address = "123 Main St";

        // Act
        CustomerEvent customerEvent = new CustomerEvent();
        customerEvent.setCustomerId(customerId);
        customerEvent.setName(name);
        customerEvent.setLegalId(legalId);
        customerEvent.setType(type);
        customerEvent.setAddress(address);

        // Assert
        Assertions.assertEquals(customerId, customerEvent.getCustomerId());
        Assertions.assertEquals(name, customerEvent.getName());
        Assertions.assertEquals(legalId, customerEvent.getLegalId());
        Assertions.assertEquals(type, customerEvent.getType());
        Assertions.assertEquals(address, customerEvent.getAddress());
    }

    @Test
    void testSerialization() {
        // Arrange
        Long customerId = 1L;
        String name = "John Doe";
        String legalId = "123456789";
        String type = "Individual";
        String address = "123 Main St";

        CustomerEvent customerEvent = new CustomerEvent(customerId, name, legalId, type, address);

        // Act
        byte[] serializedData = SerializationUtils.serialize(customerEvent);
        CustomerEvent deserializedEvent = (CustomerEvent) SerializationUtils.deserialize(serializedData);

        // Assert
        Assertions.assertEquals(customerEvent, deserializedEvent);
    }
}
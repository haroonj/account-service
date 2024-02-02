## Design Decisions
- **Customer Management Service**: [https://github.com/haroonj/customer-service](https://github.com/haroonj/customer-service)
- **Account Management Service**: [https://github.com/haroonj/account-service](https://github.com/haroonj/account-service)
### Integration of Two Microservices: Customer and Account Management
- **Approach and Technology Choice:** I integrated two microservices to manage distinct domains within the banking application: customer management and account management. Despite being mandated to use Spring Boot and Java, my strategic application of Spring Data JPA and RabbitMQ was critical for seamless data management and inter-service communication. These choices aimed at crafting a robust, scalable solution that aligns with modern microservices architectural principles.

### Database Design
- **Dual Relational Database Utilization:** I opted for two distinct MySQL databases for the customer and account management microservices to enhance the separation of concerns and scalability. This design choice was driven by the need to isolate domain-specific data, improve data management, and enhance security by segregating sensitive customer information from financial account data. Utilizing separate databases allows each microservice to operate independently, facilitating easier scaling, backups, and maintenance. This approach also supports the microservices architecture principle of decentralization, promoting each service's autonomy.

  I ensured that interactions with each database remained consistent and efficient by leveraging Spring Data JPA's capabilities for repository abstraction and entity management. This not only streamlined CRUD operations but also enforced a clean separation of data access logic, making the system more robust and adaptable to changes.

  **Benefits of Dual Database Design:**
  - **Enhanced Security:** Physically separating customer and account data minimizes risk exposure. In the event of a security breach, the impact is contained to one domain.
  - **Scalability:** Independent databases allow for the scaling of services and their underlying storage according to domain-specific load and performance requirements, optimizing resource utilization.
  - **Maintainability and Flexibility:** This eases the maintenance burden and provides the flexibility to evolve each microservice independently, including adopting different database technologies as needed to meet domain-specific requirements.
  - **Resilience:** This reduces the blast radius of database-related issues, ensuring that a problem in one domain does not directly impact the other, thereby increasing overall system resilience.

This dual-database strategy underpins my commitment to building a scalable, secure, and maintainable system, demonstrating a thoughtful approach to microservices architecture and data management.

### Event-Driven Architecture with RabbitMQ
- For the event-driven parts of our system, I chose RabbitMQ because it's robust and makes managing asynchronous communication straightforward. To streamline our processes, I set up two queues: one for handling customer creations and another for deletions. This approach not only simplifies tracking and responding to these key events but also ensures that our system can scale efficiently by focusing resources on more active queues and isolating event types for easier maintenance. Essentially, it's about keeping things organized and adaptable, ensuring each part of our service communicates smoothly and can independently adjust to changes without missing a beat.

### API Design Following OpenAPI Specification
- **REST API Standardization:** Following the Open API Specification standard was a predetermined requirement. I focused on using Springdoc OpenAPI to auto-generate documentation that is both comprehensive and compliant. Meticulous annotation of our Spring Boot controllers and models ensured the accuracy of the API documentation, facilitating both internal development and potential external integrations.

### Error Handling
- **Centralized Exception Handling with Advisor:** I implemented centralized exception handling using Spring's `@ControllerAdvice` to ensure consistent and efficient error responses across the microservices. This approach captures exceptions thrown by any controller in the application, providing a single point of configuration for error responses. The benefit of using an advisor for exception handling is it reduces code duplication by eliminating the need for repetitive try-catch blocks in each controller and ensures that all exceptions are processed uniformly, improving the API's reliability and maintainability.

  My use of `@ExceptionHandler` within the advisor class enables us to define custom responses for different types of exceptions, allowing for clear communication of error states to the client. This method enhances the API's usability by providing meaningful error messages and appropriate HTTP status codes based on the exception type encountered.

### Logging
- **Consistent Logging with SLF4J:** I adopted the Simple Logging Facade for Java (SLF4J) annotated with Lombok's `@Slf4j` for a unified logging interface that is simple to use and flexible enough to accommodate various logging implementations. The `@Slf4j` annotation automatically injects a logger instance into each class, streamlining the logging process and reducing boilerplate code.

  **Custom Logging Pattern Configuration:**
    ```
    logging.pattern.console=%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n
    ```
  I configured this logging pattern in `application.properties` to ensure that log messages are structured in a clear and informative manner. This pattern includes the timestamp, thread name, log level, logger name (truncated to 36 characters), and the log message itself. The structured format is particularly useful for debugging and monitoring, as it provides a consistent layout for log messages, making them easier to read and analyze.

  Leveraging `@Slf4j` and configuring a detailed logging pattern, I've established a robust logging framework that enhances the observability and traceability of the microservices. This approach aids in the rapid identification and resolution of issues and contributes to the overall reliability and maintainability of the application.

### Conclusion on Design Decisions

These decisions, deeply informed by the project's requirements and contemporary best practices, aimed to establish a scalable, maintainable, and efficient banking application foundation. The architecture, with a focus on clean API design, effective data handling, and robust error management, lays a solid groundwork for future enhancements and scalability.


## Shortcomings

In the journey of developing this banking application, my commitment to delivering a robust solution was accompanied by an acute awareness of areas where the implementation could be further refined. Notably:

- **Limited Event Handling:** Another area identified for improvement is the scope of event-driven interactions between microservices. Presently, the customer service only publishes events related to customer creation and deletion. This limited implementation of the event-driven architecture (EDA) does not fully exploit the potential for asynchronous, decoupled communication between services. Expanding the range of events to include account transactions and updates would significantly increase the system's responsiveness and flexibility.
- **Spring Security Integration:** One of the most significant shortcomings in the current phase of the project is the incomplete integration of Spring Security for advanced authentication and authorization mechanisms. While basic security measures are in place, the project did not fully leverage Spring Security's capabilities to implement OAuth2 for securing service-to-service communications and JWT for user authentication. This enhancement remains a priority for ensuring the highest level of security in future iterations.

### Achievements and Future Directions

Despite these shortcomings, the project successfully implemented Spring profiles, a feature that showcases the application's adaptability to different environments (development, testing, production) by altering configurations accordingly. This demonstrates a strategic approach to application deployment and management, enhancing the project's operational efficiency.

### Plans for Addressing Shortcomings

- **Enhancing Security Measures:** To address the gap in security implementations, I plan to integrate Spring Security's advanced features in the upcoming development phase. This will include setting up OAuth2 for secure service-to-service communication and utilizing JWT for robust user authentication and authorization mechanisms.

- **Expanding the Event-Driven Architecture:** I intend to extend the range of handled events beyond customer creation and deletion. By incorporating additional events such as account transactions and updates, the system will better leverage RabbitMQ for asynchronous communication, thereby enhancing the microservices architecture's decoupled nature and improving overall system resilience.

These targeted improvements are part of a continuous development strategy aimed at evolving the banking application into a more secure, responsive, and feature-complete platform.

## Assumptions

Throughout the development of this banking application, certain operational assumptions significantly influenced design and implementation choices:

1. **Automatic Account Creation**: I assumed that upon the creation of a new customer, an inactive account would be automatically generated as a default. This approach was designed to streamline the customer onboarding process, ensuring every customer has an account ready for activation, facilitating a smoother transition into engaging with our banking services.

2. **Cascading Deletion**: Another key assumption was that deleting a customer would automatically result in the deletion of all associated accounts. This decision was rooted in the desire to maintain data integrity and simplicity within our system, ensuring that we do not retain orphan accounts without an owner, which aligns with both operational efficiency and compliance with data protection regulations.

3. **Moderate Initial Load**: It was assumed that the system would face a moderate load at the outset, allowing for scalability considerations to be implemented progressively as user adoption grows.

4. **Stable External Dependencies**: The system relies on external services like RabbitMQ and the chosen relational databases operating within expected parameters. The assumption of their stability allowed us to focus on the application logic without implementing extensive fallback mechanisms for these services.


These assumptions allowed me to define clear operational workflows and automate certain processes within the application, enhancing efficiency and user experience. They represent my initial approach to simplifying customer and account management within the system, acknowledging that future iterations may revisit and refine these assumptions based on feedback and needs.

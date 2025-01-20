package com.github.caac.demo;

import dev.langchain4j.service.Result;
import dev.langchain4j.service.tool.ToolExecution;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class CustomerAgentServiceIT {

    private static final Logger logger = LoggerFactory.getLogger(CustomerAgentServiceIT.class);
    private final String email = "banana@gmail.com";
    @Autowired
    private CustomerAgentService customerAgentService;
    @Autowired
    private CustomerRepository customerRepository;

    @BeforeEach
    public void setUp() {
        createTestCustomer();
    }

    @AfterEach
    public void tearDown() {
        deleteTestCustomer();
    }

    private void createTestCustomer() {
        Customer customer = new Customer();
        customer.setName("Brother John");
        customer.setAge(40);
        customer.setEmail(email);

        customerRepository.save(customer);
    }

    private void deleteTestCustomer() {
        customerRepository.deleteAllInBatch();
    }

    @Test
    public void simple_chat() {
        String chatId = "test-chat-id";
        String userMessage = "Hello, how can I help you?";

        logger.info("Request: {}", userMessage);

        Result<String> response = customerAgentService.chat(chatId, userMessage);

        logger.info("Response: {}", response.content());

        assertNotNull(response);
    }

    @Test
    public void create_customer_chat() {
        String chatId = "test-chat-id";
        String userMessage = "Create a new customer data - email: hello@gmail.com - name: John Doe";

        logger.info("Request: {}", userMessage);

        Result<String> response = customerAgentService.chat(chatId, userMessage);

        assertNotNull(response);
        logger.info("Response: {}", response.content());

        userMessage = "Yes, I confirmed to add this record.";
        logger.info("Request: {}", userMessage);
        response = customerAgentService.chat(chatId, userMessage);

        assertNotNull(response);
        logger.info("Response: {}", response.content());

        // Check the database
        Optional<Customer> customer = customerRepository.findByEmail("hello@gmail.com");
        logger.info("Customer data: {}", customer);
    }

    @Test
    public void find_customer_by_id() {
        String chatId = "test-chat-id";
        Long customerId = 1L;
        String userMessage = "Print all data (name, email and age) of my customer data. My customer ID is: " + customerId;

        logger.info("Request: {}", userMessage);

        Result<String> response = customerAgentService.chat(chatId, userMessage);

        String answer = response.content();
        List<ToolExecution> toolExecutions = response.toolExecutions();

        assertNotNull(response);
        logger.info("Tool Executions: {}", toolExecutions);
        logger.info("Response: {}", answer);

        // Check the database
        Optional<Customer> customerChecked = customerRepository.findByEmail(email);
        logger.info("Customer data with CRUD: {}", customerChecked.get().getName());
    }

    @Test
    public void find_customer_by_email() {
        String chatId = "test-chat-id";
        String userMessage = "Print all data (name, email and age) of following customer email: " + email;

        logger.info("Request: {}", userMessage);

        Result<String> response = customerAgentService.chat(chatId, userMessage);

        String answer = response.content();
        List<ToolExecution> toolExecutions = response.toolExecutions();

        assertNotNull(response);
        logger.info("Tool Executions: {}", toolExecutions);
        logger.info("Response: {}", answer);

        // Check the database
        Optional<Customer> customerChecked = customerRepository.findByEmail(email);
        logger.info("Customer data: {}", customerChecked.get().getName());
    }

    @Test
    public void null_chat() {
        String chatId = "test-chat-id";
        String userMessage = null;

        logger.info("Request: {}", userMessage);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            customerAgentService.chat(chatId, userMessage);
        });

        logger.info(exception.getMessage());
    }
}

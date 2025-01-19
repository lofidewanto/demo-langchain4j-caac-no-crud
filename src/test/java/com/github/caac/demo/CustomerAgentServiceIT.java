package com.github.caac.demo;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import dev.langchain4j.service.Result;
import dev.langchain4j.service.tool.ToolExecution;

@SpringBootTest
public class CustomerAgentServiceIT {

    private static Logger logger = LoggerFactory.getLogger(CustomerAgentServiceIT.class);

    @Autowired
    private CustomerAgentService customerAgentService;

    @Autowired
    private CustomerRepository customerRepository;

    @Test
    public void simple_chat() {
        String chatId = "test-chat-id";
        String userMessage = "Hello, how can I help you?";

        logger.info("Request: {}", userMessage);

        Result<String> response = customerAgentService.chat(chatId, userMessage);

        logger.info("Response: {}", response);

        assertNotNull(response);
    }

    @Test
    public void create_customer_chat() {
        String chatId = "test-chat-id";
        String userMessage = "Create a new customer data - email: hello@gmail.com - name: John Doe";

        logger.info("Request: {}", userMessage);

        Result<String> response = customerAgentService.chat(chatId, userMessage);

        assertNotNull(response);
        logger.info("Response: {}", response);

        userMessage = "Yes, I confirmed to add this record.";
        logger.info("Request: {}", userMessage);
        response = customerAgentService.chat(chatId, userMessage);

        assertNotNull(response);
        logger.info("Response: {}", response);

        // Check the database
        Customer customer = customerRepository.findByEmail("hello@gmail.com");
        logger.info("Customer data: {}", customer);
    }

    @Test
    @Transactional
    public void find_customer_chat() {
        // Find a CRUD Customer Service.
        Customer customer = new Customer();
        customer.setName("Brother John");
        customer.setAge(40);

        String email = "banana@gmail.com";
        customer.setEmail(email);

        customerRepository.save(customer);

        String chatId = "test-chat-id";
        String userMessage = "Print all data (name, email and age) of following customer id: " + 1;

        logger.info("Request: {}", userMessage);

        Result<String> response = customerAgentService.chat(chatId, userMessage);

        String answer = response.content();
        List<ToolExecution> toolExecutions = response.toolExecutions();

        assertNotNull(response);
        logger.info("Tool Executions: {}", toolExecutions);
        logger.info("Response: {}", answer);
        logger.info("Response: {}", response);

        // Check the database
        Customer customerChecked = customerRepository.findByEmail(email);
        logger.info("Customer data: {}", customerChecked.getName());
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

package com.github.caac.demo;

import dev.langchain4j.service.Result;
import dev.langchain4j.service.tool.ToolExecution;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class CustomerAgentServiceIT {

    private static final Logger logger = LoggerFactory.getLogger(CustomerAgentServiceIT.class);

    private final String email = "banana@gmail.com";

    @Autowired
    private CustomerAgentService customerAgentService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AddressRepository addressRepository;

    private void createTestCustomer() {
        Customer customer = new Customer();
        customer.setName("Brother John");
        customer.setAge(40);
        customer.setEmail(email);

        Address address = new Address();
        address.setStreet("1234 Main Street");
        address.setCity("New York");
        address.setState("NY");
        address.setZipCode("10001");
        address.setCustomer(customer);

        customer.addAddress(address);
        customerRepository.save(customer);
    }

    private void deleteTestCustomer() {
        addressRepository.deleteAllInBatch();
        customerRepository.deleteAllInBatch();
    }

    @Test
    public void simple_chat() {
        String chatId = "test-chat-id" + System.currentTimeMillis();
        String userMessage = "Hello, how are you?";

        logger.info("Request: {}", userMessage);

        Result<String> response = customerAgentService.chat(chatId, userMessage);

        logger.info("Response: {}", response.content());

        assertNotNull(response);
    }

    @Disabled("Not yet correctly implemented.")
    @Test
    @Transactional
    public void create_simple_customer_chat() {
        createTestCustomer();

        String chatId = "test-chat-id" + System.currentTimeMillis();

        String userMessage = """
                I'm new here and please create a new customer for me. 
                My customer email is hello@gmail.com.
                My customer name is John Doe. 
                I'm 30 years old.
                I have no addresses.
                """;

        logger.info("Request: {}", userMessage);

        Result<String> response = customerAgentService.chat(chatId, userMessage);

        assertNotNull(response);
        logger.info("Response: {}", response.content());

        userMessage = "I confirm for the creation of my customer data.";
        response = customerAgentService.chat(chatId, userMessage);

        // Check the database
        Optional<Customer> customer = customerRepository.findByEmail("hello@gmail.com");
        assertNotNull(customer.get());
        logger.info("Customer data: {}", customer.get());

        deleteTestCustomer();
    }

    @Test
    @Transactional
    public void find_customer_by_id() {
        createTestCustomer();

        String chatId = "test-chat-id" + System.currentTimeMillis();
        Long customerId = 1L;
        String userMessage = "Print all data (name, email and age). My customer ID is: " + customerId;

        logger.info("Request: {}", userMessage);

        Result<String> response = customerAgentService.chat(chatId, userMessage);

        String answer = response.content();
        List<ToolExecution> toolExecutions = response.toolExecutions();

        assertNotNull(response);
        logger.info("Tool Executions: {}", toolExecutions);
        logger.info("Response: {}", answer);

        // Check the database
        Optional<Customer> customerChecked = customerRepository.findByEmail(email);
        logger.info("Customer data with CRUD - Customer ID: {}", customerChecked.get().getId());

        deleteTestCustomer();
    }

    @Test
    @Transactional
    public void find_customer_by_email() {
        createTestCustomer();

        String chatId = "test-chat-id" + System.currentTimeMillis();
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

        deleteTestCustomer();
    }

    @Test
    public void null_chat() {
        String chatId = "test-chat-id" + System.currentTimeMillis();
        String userMessage = null;

        logger.info("Request: {}", userMessage);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            customerAgentService.chat(chatId, userMessage);
        });

        logger.info(exception.getMessage());
    }

    @Test
    @Transactional
    public void find_all_customers() {
        createTestCustomer();

        String chatId = "test-chat-id" + System.currentTimeMillis();
        String userMessage = "Print all customers from your data.";

        logger.info("Request: {}", userMessage);

        Result<String> response = customerAgentService.chat(chatId, userMessage);

        String answer = response.content();
        List<ToolExecution> toolExecutions = response.toolExecutions();

        assertNotNull(response);
        logger.info("Tool Executions: {}", toolExecutions);
        logger.info("Response: {}", answer);

        // Check the database
        long count = customerRepository.count();
        assertEquals(count, 1);

        deleteTestCustomer();
    }
}

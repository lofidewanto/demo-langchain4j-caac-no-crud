package com.github.caac.demo;

import dev.langchain4j.service.Result;
import dev.langchain4j.service.tool.ToolExecution;
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
public class CustomerAgentIT {

    private static final Logger logger = LoggerFactory.getLogger(CustomerAgentIT.class);

    private final String bananaEmail = "banana@gmail.com";

    private final String appleEmail ="apple@gmail.com";

    @Autowired
    private CustomerAgent customerAgent;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AddressRepository addressRepository;

    private Customer createTestCustomer1() {
        Customer customer = new Customer();
        customer.setName("Brother John");
        customer.setAge(40);
        customer.setEmail(bananaEmail);

        Address address = new Address();
        address.setStreet("1234 Main Street");
        address.setCity("New York");
        address.setState("NY");
        address.setZipCode("10001");
        address.setCustomer(customer);

        customer.addAddress(address);
        return customerRepository.save(customer);
    }

    private Customer createTestCustomer2() {
        Customer customer = new Customer();
        customer.setName("Junie Tjahaja");
        customer.setAge(50);
        customer.setEmail(appleEmail);

        Address address = new Address();
        address.setStreet("1234 Horror Street");
        address.setCity("Los Angeles");
        address.setState("LA");
        address.setZipCode("90009");
        address.setCustomer(customer);

        customer.addAddress(address);
        return customerRepository.save(customer);
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

        Result<String> response = customerAgent.chat(chatId, userMessage);

        logger.info("Response: {}", response.content());

        assertNotNull(response);
    }

    @Test
    public void simple_chat_who() {
        String chatId = "test-chat-id" + System.currentTimeMillis();
        String userMessage = "Hello, who are you?";

        logger.info("Request: {}", userMessage);

        Result<String> response = customerAgent.chat(chatId, userMessage);

        logger.info("Response: {}", response.content());

        assertNotNull(response);
        assertTrue(response.content().contains("DieSoon"));
    }

    @Test
    @Transactional
    public void create_simple_customer_chat() {
        createTestCustomer1();

        String chatId = "test-chat-id" + System.currentTimeMillis();

        String userMessage = """
                I'm new here and please create a new customer for me. 
                My customer email is hello@gmail.com.
                My customer name is John Doe. 
                I'm 30 years old.
                I have no addresses.
                """;

        logger.info("Request: {}", userMessage);

        Result<String> response = customerAgent.chat(chatId, userMessage);

        assertNotNull(response);

        String answer = response.content();
        logger.info("Response: {}", answer);
        List<ToolExecution> toolExecutions = response.toolExecutions();
        logger.info("Tool Executions: {}", toolExecutions);

        // Check the database
        Optional<Customer> customer = customerRepository.findByEmail("hello@gmail.com");
        assertNotNull(customer.get());
        logger.info("Customer data: {}", customer.get());
        assertTrue(answer.contains("id"));

        deleteTestCustomer();
    }

    @Test
    @Transactional
    public void find_customer_by_id() {
        Customer customer = createTestCustomer1();
        Long customerId = customer.getId();

        String chatId = "test-chat-id" + System.currentTimeMillis();
        String userMessage = """
            My password is admin. 
            Print all data (name, email and age). 
            My customer ID is: """ + customerId + ".";

        logger.info("Request: {}", userMessage);

        Result<String> response = customerAgent.chat(chatId, userMessage);

        String answer = response.content();
        List<ToolExecution> toolExecutions = response.toolExecutions();

        assertNotNull(response);
        logger.info("Tool Executions: {}", toolExecutions);
        logger.info("Response: {}", answer);
        assertTrue(answer.contains(Long.toString(customerId)));

        // Check the database
        Optional<Customer> customerChecked = customerRepository.findByEmail(bananaEmail);
        Long id = customerChecked.get().getId();
        logger.info("Customer data with CRUD - Customer ID: {}", id);
        assertEquals(customerId, id);

        deleteTestCustomer();
    }

    @Test
    @Transactional
    public void find_customer_by_email() {
        createTestCustomer1();

        String chatId = "test-chat-id" + System.currentTimeMillis();
        String userMessage = """
            My password is admin. 
            Print all data (name, email and age) of following customer email: """ + bananaEmail + ".";

        logger.info("Request: {}", userMessage);

        Result<String> response = customerAgent.chat(chatId, userMessage);

        String answer = response.content();
        List<ToolExecution> toolExecutions = response.toolExecutions();

        assertNotNull(response);
        logger.info("Tool Executions: {}", toolExecutions);
        logger.info("Response: {}", answer);

        // Check the database
        Optional<Customer> customerChecked = customerRepository.findByEmail(bananaEmail);
        logger.info("Customer data: {}", customerChecked.get().getName());

        deleteTestCustomer();
    }

    @Test
    public void null_chat() {
        String chatId = "test-chat-id" + System.currentTimeMillis();
        String userMessage = null;

        logger.info("Request: {}", userMessage);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            customerAgent.chat(chatId, userMessage);
        });

        logger.info(exception.getMessage());
    }

    @Test
    @Transactional
    public void find_all_customers() {
        createTestCustomer1();
        createTestCustomer2();

        String chatId = "test-chat-id" + System.currentTimeMillis();
        String userMessage = "Print all customers from your data. Show them to me.";

        logger.info("Request: {}", userMessage);

        Result<String> response = customerAgent.chat(chatId, userMessage);

        String answer = response.content();
        List<ToolExecution> toolExecutions = response.toolExecutions();

        assertNotNull(response);
        logger.info("Tool Executions: {}", toolExecutions);
        logger.info("Response: {}", answer);

        // Check the database
        long count = customerRepository.count();
        assertEquals(count, 2);

        deleteTestCustomer();
    }
}

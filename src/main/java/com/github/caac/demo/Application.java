package com.github.caac.demo;

import java.util.List;
import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import dev.langchain4j.service.Result;

@SpringBootApplication
public class Application implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(Application.class);

    @Autowired
    private CustomerAgent customerAgent;

    @Autowired
    private CustomerService customerService;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("Starting the application...");
        createTestCustomer();

        String chatId = "interactive-chat-id" + System.currentTimeMillis();

        // Turn on this line to use the chat in the console.
        // args = new String[] { "chat-id" };

        if (args.length <= 0) {
            logger.info("Usage: java -jar demo-langchain4j-caac-no-crud-0.0.1-SNAPSHOT.jar <chat-id>");

            // deleteTestCustomer();

            return;
        }

        // Create an Agentic Customer Service.
        Scanner scanner = new Scanner(System.in);

        logger.info("");
        logger.info("Mila - Chat Customer Support for DieSoon Company.");

        while (true) {
            logger.info("");
            logger.info("You: ");
            String userMessage = scanner.nextLine();
            logger.info("");

            if ("exit".equalsIgnoreCase(userMessage)) {
                break;
            }

            Result<String> response = customerAgent.chat(chatId, userMessage);
            logger.info("Agent: " + response.content());
            logger.info("");
        }

        scanner.close();
    }

    private void createTestCustomer() {
        Customer customer = new Customer();
        customer.setName("Brandy Doe");
        customer.setAge(40);
        customer.setEmail("john@gmail.com");

        Address address = new Address();
        address.setStreet("1234 Main Street");
        address.setCity("New York");
        address.setState("NY");
        address.setZipCode("10001");

        customerService.createCustomerWithAddress(customer, List.of(address));
    }

    private void deleteTestCustomer() {
        customerService.deleteAllCustomers();
    }
}

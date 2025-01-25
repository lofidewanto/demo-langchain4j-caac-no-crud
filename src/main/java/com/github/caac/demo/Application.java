package com.github.caac.demo;

import dev.langchain4j.service.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    private CustomerAgentService customerAgentService;

    @Autowired
    private CustomerCrudService customerCrudService;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        createTestCustomer();

        String chatId = "interactive-chat-id" + System.currentTimeMillis();

        // Turn on this line to use the chat in the console.
        // args = new String[] { "chat-id" };

        if (args.length <= 0) {
            System.out.println("Usage: java -jar demo-langchain4j-caac-no-crud-0.0.1-SNAPSHOT.jar <chat-id>");

            deleteTestCustomer();

            return;
        }

        // Create an Agentic Customer Service.
        Scanner scanner = new Scanner(System.in);

        System.out.println();
        System.out.print("Chat Customer Support for DieSoon Company.\n");

        while (true) {
            System.out.println();
            System.out.print("You: ");
            String userMessage = scanner.nextLine();
            System.out.println();

            if ("exit".equalsIgnoreCase(userMessage)) {
                break;
            }

            Result<String> response = customerAgentService.chat(chatId, userMessage);
            System.out.println("Agent: " + response.content());
            System.out.println();
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

        customerCrudService.createCustomerWithAddress(customer, List.of(address));
    }

    private void deleteTestCustomer() {
        customerCrudService.deleteAllCustomers();
    }
}

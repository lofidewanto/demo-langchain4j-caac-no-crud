package com.github.caac.demo;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoLangchain4jCaacNoCrudApplication implements CommandLineRunner {

    @Autowired
    private CustomerAgentService customerAgentService;

    public static void main(String[] args) {
        SpringApplication.run(DemoLangchain4jCaacNoCrudApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        String chatId = "interactive-chat-id";
        
        // Turn on this line to use the chat in the console.
        // args = new String[] { "chat-id" };

        if (args.length <= 0) {
            System.out.println("Usage: java -jar demo-langchain4j-caac-no-crud-0.0.1-SNAPSHOT.jar <chat-id>");
            return;
        }

        Scanner scanner = new Scanner(System.in);

        System.out.println();
        System.out.print("Chat Customer Support for DieSoon Company.\n");
        System.out.println();
        
        while (true) {
            System.out.println();
            System.out.print("You: ");
            String userMessage = scanner.nextLine();
            System.out.println();
            
            if ("exit".equalsIgnoreCase(userMessage)) {
                break;
            }
            
            String response = customerAgentService.chat(chatId, userMessage);
            System.out.println("Agent: " + response);
            System.out.println();
        }
        
        scanner.close();
    }
}

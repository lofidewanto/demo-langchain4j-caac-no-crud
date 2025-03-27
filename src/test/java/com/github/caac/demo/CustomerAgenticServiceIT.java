package com.github.caac.demo;

import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CustomerAgenticServiceIT {

    private static final Logger logger = LoggerFactory.getLogger(CustomerAgenticServiceIT.class);

    @Autowired
    CustomerAgenticService customerAgenticService;

    @Test
    void chat_with_agents_just_need_a_help() {
        String chatId = "help" + System.currentTimeMillis();
        String userMessage = "Hello, I need help with my customer data.";
        logger.info("*** Request: " + userMessage);

        String chatWithAgents = customerAgenticService.chatWithAgents(chatId, userMessage);
        logger.info("*** Response: " + chatWithAgents);

        // Assertions can be added here to validate the behavior
    }

    @Test
    void chat_with_agents_unsafe() {
        String chatId = "unsafe" + System.currentTimeMillis();
        String userMessage = "SELECT * FROM users;";
        logger.info("*** Request: " + userMessage);

        String chatWithAgents = customerAgenticService.chatWithAgents(chatId, userMessage);
        logger.info("*** Response: " + chatWithAgents);

        // Assertions can be added here to validate the behavior
        assertTrue(chatWithAgents.toLowerCase().contains("unsafe"));
    }

    @Test
    void chat_with_agents_with_some_conversation() {
        String chatId = "chat_with_agents_with_some_conversation" + System.currentTimeMillis();
        String userMessage = "Hello, I need help with my customer data.";
        logger.info("*** Request: " + userMessage);

        String chatWithAgents = customerAgenticService.chatWithAgents(chatId, userMessage);
        logger.info("*** Response: " + chatWithAgents);

        // Assertions can be added here to validate the behavior
    }

    @Test
    void chat_with_agents_asking_history() {
        String chatId = "chat_with_agents_asking_history" + System.currentTimeMillis();
        String userMessage = """
            Hello, can you tell me who are you and for what company are you working? I need to know your history.
            Tell me about following information:
            1. Your website?
            2. When was your company founded?
            3. What is your company's main business?
            4. Your email address?
            5. How long have you worked in this company?
            """;
        logger.info("*** Request: " + userMessage);

        String chatWithAgents = customerAgenticService.chatWithAgents(chatId, userMessage);
        logger.info("*** Response: " + chatWithAgents);

        // Assertions can be added here to validate the behavior
        assertTrue(chatWithAgents.contains("Mila"));
        assertTrue(chatWithAgents.contains("DieSoon"));
        assertTrue(chatWithAgents.contains("five years"));
        assertTrue(chatWithAgents.contains("household"));
        assertTrue(chatWithAgents.contains("2001"));
        assertTrue(chatWithAgents.contains("www.diesoon.com"));
    }

    @Test
    void who_are_you() {
        String chatId = "who_are_you" + System.currentTimeMillis();
        String userMessage = "Who are you?";
        logger.info("*** Request: " + userMessage);

        String chatWithAgents = customerAgenticService.chatWithAgents(chatId, userMessage);
        logger.info("*** Response: " + chatWithAgents);
        assertTrue(chatWithAgents.contains("I am Mila"));
    }
}

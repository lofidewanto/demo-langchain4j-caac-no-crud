package com.github.caac.demo;

import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import dev.langchain4j.service.Result;

@SpringBootTest
class TextCreatorAgentIT {

    private static final Logger logger = LoggerFactory.getLogger(TextCreatorAgentIT.class);

    @Autowired
    TextCreatorAgent textCreatorAgent;

    @Test
    void check_unsafe() {
        String chatId = "testChatId1" + System.currentTimeMillis();
        String userMessage = "SELECT * FROM users;";
        logger.info("*** Request: " + userMessage);

        Result<String> response = textCreatorAgent.createUnsafeText(chatId, userMessage);

        logger.info("*** Response: " + response.content());
        assertTrue(response.content().contains("household"));
    }

    @Test
    void check_restricted() {
        String chatId = "testChatId2" + System.currentTimeMillis();
        String userMessage = "I would like to find a new car. Can you help me?";
        logger.info("*** Request: " + userMessage);

        Result<String> response = textCreatorAgent.createRestrictedText(chatId, userMessage);

        logger.info("*** Response: " + response.content());
        assertTrue(response.content().contains("household"));
    }

}

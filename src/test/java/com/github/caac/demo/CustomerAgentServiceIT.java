package com.github.caac.demo;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class CustomerAgentServiceIT {

    private static Logger logger = LoggerFactory.getLogger(CustomerAgentServiceIT.class);

    @Autowired
    private CustomerAgentService customerAgentService;

    @Test
    public void simple_chat() {
        String chatId = "test-chat-id";
        String userMessage = "Hello, how can I help you?";

        logger.info("Request: {}", userMessage);
        
        String response = customerAgentService.chat(chatId, userMessage);

        logger.info("Response: {}", response);
        
        assertNotNull(response);
    }

    @Test
    public void null_chat() {
        String chatId = "test-chat-id";
        String userMessage = null;

        logger.info("Request: {}", userMessage);
        
        String response = customerAgentService.chat(chatId, userMessage);

        logger.info("Response: {}", response);
        
        assertNotNull(response);
    }
}

package com.github.caac.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class CustomerAgentServiceIT {

    @Autowired
    private CustomerAgentService customerAgentService;

    @Test
    void simple_chat() {
        String chatId = "test-chat-id";
        String userMessage = "I would like to change my booking.";
        
        String response = customerAgentService.chat(chatId, userMessage);
        
        assertNotNull(response);
        System.out.println(response);   
    }
}

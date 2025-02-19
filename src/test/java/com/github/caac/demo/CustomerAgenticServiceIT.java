package com.github.caac.demo;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CustomerAgenticServiceIT {

    @Autowired
    CustomerAgenticService customerAgenticService;

    @Test
    void chat_with_agents_just_need_a_help() {
        String userMessage = "Hello, I need help with my booking.";
        System.out.println("*** Request: " + userMessage);

        String chatWithAgents = customerAgenticService.chatWithAgents(userMessage);
        System.out.println("*** Response: " + chatWithAgents);

        // Assertions can be added here to validate the behavior
    }
}

package com.github.caac.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import dev.langchain4j.service.Result;

@Service
public class CustomerAgenticService {

    private static final Logger logger = LoggerFactory.getLogger(CustomerAgenticService.class);

    private CustomerAgent customerAgent;

    private ProtectorAgent protectorAgent;

    public CustomerAgenticService(CustomerAgent customerAgent,
            ProtectorAgent protectorAgent) {
        this.customerAgent = customerAgent;
        this.protectorAgent = protectorAgent;
    }

    public String chatWithAgents(String userMessage) {
        // 1. Check the incoming message with the ProtectorAgent
        // 2. If the message is not a threat, check with the CustomerAgent

        String result = protectorAgent.check("protector", userMessage);
        if (result.toLowerCase().contains("unsafe")) {
            logger.warn("ProtectorAgent: The message is not safe.");

            return "The message is unsafe.";
        } else {
            logger.info("The message is safe.");

            Result<String> chatCustomer = customerAgent.chat("customer", userMessage);
            logger.info("*** CustomerAgent content: {}", chatCustomer.content());
            logger.info("*** CustomerAgent tools: {}", chatCustomer.toolExecutions());

            return chatCustomer.content();
        }
    }
}

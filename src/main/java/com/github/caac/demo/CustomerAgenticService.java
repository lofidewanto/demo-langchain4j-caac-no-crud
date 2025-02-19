package com.github.caac.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import dev.langchain4j.service.Result;

import java.util.List;
import java.util.Optional;

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
        if (result.equalsIgnoreCase("unsafe")) {
            logger.warn("ProtectorAgent: The message is not safe.");

            return "The message is not safe.";
        } else {
            logger.info("The message is safe.");

            Result<String> chatCustomer = customerAgent.chat("customer", userMessage);
            logger.info("*** CustomerAgent content: {}", chatCustomer.content());
            logger.info("*** CustomerAgent tools: {}", chatCustomer.toolExecutions());

            return chatCustomer.content();
        }
    }
}

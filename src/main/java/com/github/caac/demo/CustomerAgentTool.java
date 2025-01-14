package com.github.caac.demo;

import org.springframework.stereotype.Component;

import dev.langchain4j.agent.tool.Tool;

@Component
public class CustomerAgentTool {

    private final CustomerRepository customerRepository;

    public CustomerAgentTool(CustomerRepository service) {
        this.customerRepository = service;
    }

    @Tool("""
            Retrieves a customer by the email.
            """)
    public Customer getCustomer(String email) {
        return customerRepository.findByEmail(email);
    }

}

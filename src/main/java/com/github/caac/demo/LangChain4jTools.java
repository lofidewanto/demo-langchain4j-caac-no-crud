package com.github.caac.demo;

import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class LangChain4jTools {

    private final CustomerRepository customerRepository;

    public LangChain4jTools(CustomerRepository service) {
        this.customerRepository = service;
    }

    @Tool("""
            Retrieves a customer by the email.
            """)
    public Customer getCustomer(String email) {
        return customerRepository.findByEmail(email);
    }

}

package com.github.caac.demo;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
class CustomerControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    void get_customer() throws Exception {
        mockMvc.perform(get("/customer_crud/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    void create_customer_with_address() throws Exception {
        Customer customer = new Customer();
        customer.setName("John Doe");
        customer.setAge(50);
        customer.setEmail("lofi@mail.com");

        Address address1 = new Address();
        address1.setStreet("123 Main St");
        address1.setCity("Anytown");

        Address address2 = new Address();
        address2.setStreet("456 Oak St");
        address2.setCity("Othertown");

        mockMvc.perform(post("/customer_crud")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customer))
                .param("addresses", objectMapper.writeValueAsString(Arrays.asList(address1, address2))))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }
    
}

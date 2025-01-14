package com.github.caac.demo;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.ollama.OllamaContainer;

@SpringBootTest
class CustomerAgentServiceIT {

    private static OllamaContainer ollamaContainer;

    @Autowired
    private CustomerAgentService customerAgentService;

    private static int portOllama = 11434;

    @SuppressWarnings("resource")
    @BeforeAll
    static void setUp() {
        ollamaContainer = new OllamaContainer("ollama/ollama")
                .withExposedPorts(portOllama)
                .waitingFor(Wait.forListeningPort())
                .withCommand("serve");

        ollamaContainer.start();
    }

    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        String ollamaHost = ollamaContainer.getHost();
        Integer ollamaPort = ollamaContainer.getMappedPort(portOllama);

        String baseUrl = "http://localhost:" + ollamaPort;

        System.out.println("Ollama is running on: " + ollamaHost + ":" + ollamaPort);
        assertTrue(ollamaHost != null && ollamaPort != null);

        registry.add("langchain4j.ollama.chat-model.base-url", () -> baseUrl);
    }

    @AfterAll
    static void tearDown() {
        if (ollamaContainer != null) {
            ollamaContainer.stop();
        }
    }

    @Test
    void downloadLlamaModel() throws Exception {
        int ollamaPort = ollamaContainer.getMappedPort(portOllama);
        String baseUrl = "http://localhost:" + ollamaPort;

        HttpClient client = HttpClient.newHttpClient();

        String payload = "{ \"model\": \"llama3.1\" }";
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/download"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            System.out.println("Model Llama 3.1 downloaded successfully.");
        } else {
            throw new Exception("Failed to download the model: " + response.body());
        }

        verifyModel();
    }

    void verifyModel() throws Exception {
        int ollamaPort = ollamaContainer.getMappedPort(portOllama);
        String baseUrl = "http://localhost:" + ollamaPort;

        HttpClient client = HttpClient.newHttpClient();
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/models"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        System.out.println("Available models: " + response.body());
    }

    @Test
    void pullLlamaModel() throws Exception {
        int ollamaPort = ollamaContainer.getMappedPort(portOllama);
        String baseUrl = "http://localhost:" + ollamaPort;
    
        HttpClient client = HttpClient.newHttpClient();
    
        String payload = "{ \"model\": \"llama-3.1\" }";
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/pull"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .build();
    
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            System.out.println("Successfully pulled Llama 3.1 model.");
        } else {
            throw new RuntimeException("Failed to pull model: " + response.body());
        }
    }

    @Test
    void simple_chat() {
        String chatId = "test-chat-id";
        String userMessage = "I would like to change my booking.";

        String response = customerAgentService.chat(chatId, userMessage);

        assertNotNull(response);
        System.out.println(response);
    }
}

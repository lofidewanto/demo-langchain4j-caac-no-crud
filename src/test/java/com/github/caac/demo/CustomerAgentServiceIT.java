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
import org.testcontainers.utility.MountableFile;

@SpringBootTest
class CustomerAgentServiceIT {

    private static OllamaContainer ollamaContainer;

    @Autowired
    private CustomerAgentService customerAgentService;

    private static int portOllama = 11434;

    private String payload = "{ \"model\": \"llama3.2\" }";

    private static String image = "ollama/ollama";

    private static String httpAddress = "http://localhost:";

    @SuppressWarnings("resource")
    @BeforeAll
    static void setUp() {
        ollamaContainer = new OllamaContainer(image)
                .withExposedPorts(portOllama)
                .waitingFor(Wait.forListeningPort())
                .withCommand("serve")
                .withCreateContainerCmdModifier(cmd -> cmd.getHostConfig()
                        .withMemory(7L * 1024 * 1024 * 1024))
                .withCopyFileToContainer(
                        MountableFile.forHostPath("target/ollama-models"),
                        "/var/lib/ollama/models");
        ollamaContainer.start();
    }

    @DynamicPropertySource
    static void langChain4jProperties(DynamicPropertyRegistry registry) {
        String ollamaHost = ollamaContainer.getHost();
        Integer ollamaPort = ollamaContainer.getMappedPort(portOllama);

        String baseUrl = httpAddress + ollamaPort;

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

    void verifyModel() throws Exception {
        int ollamaPort = ollamaContainer.getMappedPort(portOllama);
        String baseUrl = httpAddress + ollamaPort;

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest listRequest = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/api/tags"))
                .GET()
                .build();

        HttpResponse<String> listResponse = client.send(listRequest, HttpResponse.BodyHandlers.ofString());

        System.out.println("Available models: " + listResponse.body());
    }

    void pullLlamaModel() throws Exception {
        int ollamaPort = ollamaContainer.getMappedPort(portOllama);
        String baseUrl = httpAddress + ollamaPort;

        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(baseUrl + "/api/pull"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            System.out.println("Successfully pulled Llama 3.x model.");
        } else {
            throw new RuntimeException("Failed to pull model: " + response.body());
        }

        verifyModel();
    }

    @Test
    void simple_chat() throws Exception {
        pullLlamaModel();

        String chatId = "test-chat-id";
        String userMessage = "I would like to change my booking.";

        String response = customerAgentService.chat(chatId, userMessage);

        assertNotNull(response);
        System.out.println("Simple Chat: " + response);
    }
}

package com.github.caac.demo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

import com.twilio.Twilio;

import dev.langchain4j.data.segment.TextSegment;
import dev.langchain4j.memory.chat.ChatMemoryProvider;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.model.embedding.onnx.allminilml6v2.AllMiniLmL6V2EmbeddingModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.rag.content.retriever.ContentRetriever;
import dev.langchain4j.rag.content.retriever.EmbeddingStoreContentRetriever;
import dev.langchain4j.store.embedding.EmbeddingStore;
import dev.langchain4j.store.embedding.inmemory.InMemoryEmbeddingStore;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@EnableAsync
@Configuration
public class ApplicationConfig {

    @Value("${spring.application.name}")
    private String applicationName;

    public ApplicationConfig(@Value("${twilio.account.sid}") String accountSid,
        @Value("${twilio.auth.token}") String authToken) {
        Twilio.init(accountSid, authToken);
    }

    @Bean
    OllamaChatModel customerOllamaChatModel(
            @Value("${langchain4j.ollama.chat-model.customer.base-url}") String baseUrl,
            @Value("${langchain4j.ollama.chat-model.customer.model-name}") String modelName,
            @Value("${langchain4j.ollama.chat-model.customer.log-requests}") boolean logRequests,
            @Value("${langchain4j.ollama.chat-model.customer.log-responses}") boolean logResponses,
            @Value("${langchain4j.ollama.chat-model.customer.temperature}") double temperature) {
        return OllamaChatModel.builder()
                .baseUrl(baseUrl)
                .modelName(modelName)
                .logRequests(logRequests)
                .logResponses(logResponses)
                .temperature(temperature)
                .build();
    }

    @Bean
    OllamaChatModel protectorOllamaChatModel(
            @Value("${langchain4j.ollama.chat-model.protector.base-url}") String baseUrl,
            @Value("${langchain4j.ollama.chat-model.protector.model-name}") String modelName,
            @Value("${langchain4j.ollama.chat-model.protector.log-requests}") boolean logRequests,
            @Value("${langchain4j.ollama.chat-model.protector.log-responses}") boolean logResponses,
            @Value("${langchain4j.ollama.chat-model.protector.temperature}") double temperature) {
        return OllamaChatModel.builder()
                .baseUrl(baseUrl)
                .modelName(modelName)
                .logRequests(logRequests)
                .logResponses(logResponses)
                .temperature(temperature)
                .build();
    }

    @Bean
    OllamaChatModel textCreatorOllamaChatModel(
            @Value("${langchain4j.ollama.chat-model.textcreator.base-url}") String baseUrl,
            @Value("${langchain4j.ollama.chat-model.textcreator.model-name}") String modelName,
            @Value("${langchain4j.ollama.chat-model.textcreator.log-requests}") boolean logRequests,
            @Value("${langchain4j.ollama.chat-model.textcreator.log-responses}") boolean logResponses,
            @Value("${langchain4j.ollama.chat-model.textcreator.temperature}") double temperature) {
        return OllamaChatModel.builder()
                .baseUrl(baseUrl)
                .modelName(modelName)
                .logRequests(logRequests)
                .logResponses(logResponses)
                .temperature(temperature)
                .build();
    }

    @Bean
    OpenAiChatModel customerOpenAiChatModel(
        @Value("${langchain4j.open-ai.chat-model.customer.base-url}") String baseUrl,
            @Value("${langchain4j.open-ai.chat-model.customer.model-name}") String modelName,
            @Value("${langchain4j.open-ai.chat-model.customer.log-requests}") boolean logRequests,
            @Value("${langchain4j.open-ai.chat-model.customer.log-responses}") boolean logResponses,
            @Value("${langchain4j.open-ai.chat-model.customer.temperature}") double temperature,
            @Value("${langchain4j.open-ai.chat-model.customer.api-key}") String apiKey) {
        return OpenAiChatModel.builder()
                .baseUrl(baseUrl)
                .modelName(modelName)
                .logRequests(logRequests)
                .logResponses(logResponses)
                .temperature(temperature)
                .apiKey(apiKey)
                .build();
    }

    @Bean
    ChatMemoryProvider customerChatMemoryProvider() {
        return chatId -> MessageWindowChatMemory.withMaxMessages(10);
    }

    @Bean
    ChatMemoryProvider protectorChatMemoryProvider() {
        return chatId -> MessageWindowChatMemory.withMaxMessages(10);
    }

    @Bean
    ChatMemoryProvider textCreatorChatMemoryProvider() {
        return chatId -> MessageWindowChatMemory.withMaxMessages(1);
    }

    @Bean
    EmbeddingStore<TextSegment> embeddingStore() {
        return new InMemoryEmbeddingStore<>();
    }

    @Bean
    EmbeddingModel embeddingModel() {
        EmbeddingModel embeddingModel = new AllMiniLmL6V2EmbeddingModel();
        return embeddingModel;
    }

    @Bean
    ContentRetriever contentRetriever(
            EmbeddingStore<TextSegment> embeddingStore,
            EmbeddingModel embeddingModel) {
        return EmbeddingStoreContentRetriever.builder()
                .embeddingStore(embeddingStore)
                .embeddingModel(embeddingModel)
                .maxResults(5)
                .minScore(0.9)
                .build();
    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(applicationName)
                        .version("1.0")
                        .description("Mila Chatbot REST APIs"));
    }
}

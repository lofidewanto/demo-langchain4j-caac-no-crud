package com.github.caac.demo;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.Result;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;
import dev.langchain4j.service.spring.AiServiceWiringMode;

@AiService(wiringMode = AiServiceWiringMode.EXPLICIT, 
        chatMemoryProvider = "textCreatorChatMemoryProvider", 
        chatModel = "textCreatorOllamaChatModel")
public interface TextCreatorAgent {

    @SystemMessage(fromResource = "text-unsafe-creator-agent.txt")
    Result<String> createUnsafeText(@MemoryId String chatId, @UserMessage String userMessage);

    @SystemMessage(fromResource = "text-restricted-creator-agent.txt")
    Result<String> createRestrictedText(@MemoryId String chatId, @UserMessage String userMessage);

    @SystemMessage(fromResource = "text-waiting-creator-agent.txt")
    Result<String> createWaitingText(@MemoryId String chatId, @UserMessage String userMessage);
    
}

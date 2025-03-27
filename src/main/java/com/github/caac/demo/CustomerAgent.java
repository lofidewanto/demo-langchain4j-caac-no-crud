package com.github.caac.demo;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.Result;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;
import dev.langchain4j.service.spring.AiServiceWiringMode;

@AiService(wiringMode = AiServiceWiringMode.EXPLICIT, 
            chatMemoryProvider = "customerChatMemoryProvider", 
            chatModel = "customerOpenAiChatModel",
            tools = {"customerTool"},
            contentRetriever = "contentRetriever")
public interface CustomerAgent {

    @SystemMessage(fromResource = "customer-agent.txt")
    Result<String> chat(@MemoryId String chatId, @UserMessage String userMessage);
}

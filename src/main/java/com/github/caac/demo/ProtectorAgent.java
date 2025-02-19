package com.github.caac.demo;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.Result;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;
import dev.langchain4j.service.spring.AiServiceWiringMode;

@AiService(wiringMode = AiServiceWiringMode.EXPLICIT, 
            chatMemoryProvider = "protectorChatMemoryProvider", 
            chatModel = "protectorOllamaChatModel")
public interface ProtectorAgent {

    @SystemMessage(fromResource = "protector-agent.txt")
    Result<String> chat(@MemoryId String chatId, @UserMessage String userMessage);
}

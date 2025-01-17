package com.github.caac.demo;

import dev.langchain4j.service.MemoryId;
import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;
import dev.langchain4j.service.spring.AiService;

@AiService
public interface CustomerAgentService {

    @SystemMessage(fromResource = "company-rules.txt")
    String chat(@MemoryId String chatId, @UserMessage String userMessage);
}

package com.github.caac.demo;

import java.util.List;

import org.springframework.context.ApplicationListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import dev.langchain4j.agent.tool.ToolSpecification;
import dev.langchain4j.service.spring.event.AiServiceRegisteredEvent;

@Component
public class AiServiceRegisteredEventListener implements ApplicationListener<AiServiceRegisteredEvent> {

    @Override
    public void onApplicationEvent(@NonNull AiServiceRegisteredEvent event) {
        Class<?> aiServiceClass = event.aiServiceClass();
        List<ToolSpecification> toolSpecifications = event.toolSpecifications();

        int index = 1;
        for (ToolSpecification toolSpecification : toolSpecifications) {
            System.out.printf("[%s]: [Tool-%d]: %s%n", "Tools: " + aiServiceClass.getSimpleName(), index++,
                    toolSpecification);
        }
    }
}
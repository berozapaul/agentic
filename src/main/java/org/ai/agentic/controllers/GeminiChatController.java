package org.ai.agentic.controllers;

import org.ai.agentic.config.AiConfig;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GeminiChatController {

    private final ChatClient chatClient;

    // The Builder is automatically provided by the Spring AI Starter
    public GeminiChatController(ChatClient.Builder builder, AiConfig.UcpTools ucpTools) {
        try {
            this.chatClient = builder
                    .defaultTools(ucpTools)
                    .build();
        } catch (Exception e) {
            System.err.println("CRITICAL ERROR: " + e.getMessage());
            throw e;
        }
    }

    @GetMapping("/ai/shop")
    public String shop(@RequestParam String message) {
        return this.chatClient.prompt()
                .user(message)
                .call()
                .content();
    }
}
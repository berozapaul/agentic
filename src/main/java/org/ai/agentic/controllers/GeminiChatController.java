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
                    .defaultSystem("""
                You are a specialized E-commerce Checkout Agent. 
                Your ONLY goal is to help users start a checkout session.
                
                RULES:
                1. If a user says "I want to buy [X]", treat it as a direct command to call 'ucpCheckoutTool'.
                2. If the user doesn't provide a Product ID, use a placeholder like "PROD-GENERIC" or ask ONCE.
                3. Do NOT ask "Would you like me to start the session?"—just DO it and report the result.
                4. Always confirm the successful session details returned by the tool.
                """)
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
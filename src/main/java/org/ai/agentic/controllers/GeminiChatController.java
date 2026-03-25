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
                You are the UCP Autonomous Purchasing Agent.
                
                YOUR CAPABILITIES:
                - You can create AND complete orders in one go if the system allows it.
                
                RESPONSE GUIDELINES:
                1. If the tool returns "Order Automatically Completed", celebrate with the user and provide their Confirmation ID in bold.
                2. If the tool returns "Escalation Required", provide the link and explain WHY (e.g., "I need you to verify the terms").
                3. Use a professional, efficient tone.
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
package org.ai.agentic.config;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Configuration
public class AiConfig {

    @Bean
    public UcpTools ucpTools(RestClient.Builder restClientBuilder) {
        return new UcpTools(restClientBuilder.baseUrl("http://localhost:8080").build());
    }

    public static class UcpTools {
        private final RestClient restClient;

        public UcpTools(RestClient restClient) {
            this.restClient = restClient;
        }

        @Tool(description = "Creates a real UCP checkout session. Use this when the user wants to buy something.")
        public String ucpCheckoutTool(String productId, int quantity) {
            System.out.println("LOG: Agent calling UCP API for " + productId);

            // 1. Call your actual AgenticCheckoutController
            Map<String, Object> requestBody = Map.of(
                    "productId", productId,
                    "quantity", quantity
            );

            try {
                Map response = restClient.post()
                        .uri("/api/v1/ucp/checkout-sessions")
                        .body(requestBody)
                        .retrieve()
                        .body(Map.class);

                // 2. Return the structured data back to Gemini
                return String.format(
                        "Session Created! ID: %s, Status: %s, URL: %s",
                        response.get("id"),
                        response.get("status"),
                        response.get("continue_url")
                );
            } catch (Exception e) {
                return "Error connecting to UCP Service: " + e.getMessage();
            }
        }
    }
}
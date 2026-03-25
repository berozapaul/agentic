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

        @Tool(description = "Creates and completes a UCP checkout. If escalation is required, it returns a link.")
        public String ucpCheckoutTool(String productId, int quantity) {
            System.out.println("LOG: Agent initiating Auto-Checkout for " + productId);

            try {
                // STEP 1: Create the Session
                Map<String, Object> requestBody = Map.of("productId", productId, "quantity", quantity);
                Map<String, Object> sessionResponse = restClient.post()
                        .uri("/api/v1/ucp/checkout-sessions")
                        .body(requestBody)
                        .retrieve()
                        .body(Map.class);

                String sessionId = (String) sessionResponse.get("id");
                String status = (String) sessionResponse.get("status");

                // STEP 2: Logical Branching
                if ("ready_for_complete".equals(status)) {
                    System.out.println("LOG: Status is READY. Agent is completing order " + sessionId);

                    // Call the /complete endpoint automatically
                    Map<String, Object> completionResponse = restClient.post()
                            .uri("/api/v1/ucp/checkout-sessions/{id}/complete", sessionId)
                            .retrieve()
                            .body(Map.class);

                    return String.format(
                            "Order Automatically Completed! ID: %s. Message: %s",
                            completionResponse.get("order_id"),
                            completionResponse.get("message")
                    );
                } else {
                    // Fallback: If status was 'incomplete' or 'requires_escalation'
                    return "Escalation Required. Please complete here: " + sessionResponse.get("continue_url");
                }
            } catch (Exception e) {
                return "Error in Agentic Flow: " + e.getMessage();
            }
        }
    }
}
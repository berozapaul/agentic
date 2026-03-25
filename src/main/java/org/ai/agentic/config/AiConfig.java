package org.ai.agentic.config;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AiConfig {

    // Define the record as a separate class or static inside
    public record CheckoutRequest(String productId, int quantity) {}

    @Bean
    public UcpTools ucpTools() {
        return new UcpTools();
    }

    public static class UcpTools {

        @Tool(description = "Starts a UCP checkout session for a specific product and quantity")
        public String ucpCheckoutTool(String productId, int quantity) {
            // Your logic here
            System.out.println("AI is calling UCP Checkout for: " + productId);
            return "Successfully initiated UCP Session for " + quantity + " x " + productId;
        }
    }
}
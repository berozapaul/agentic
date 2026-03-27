package org.ai.agentic.config;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

@Configuration
public class AiConfig {

    @Bean
    public UcpTools ucpTools(RestClient.Builder restClientBuilder) {
        return new UcpTools(restClientBuilder.baseUrl("http://localhost:8080").build());
    }

    public static class UcpTools {
        private final RestClient restClient;
        private Map<String, Object> cachedManifest;

        public UcpTools(RestClient restClient) {
            this.restClient = restClient;
        }

        private String resolveEndpoint(String intent) {
            if (cachedManifest == null) {
                System.out.println("LOG: Initializing UCP Discovery via /.well-known/ucp...");
                this.cachedManifest = restClient.get()
                        .uri("/.well-known/ucp")
                        .retrieve()
                        .body(Map.class);
            }

            try {
                List<Map<String, Object>> capabilities = (List<Map<String, Object>>) cachedManifest.get("capabilities");
                Map<String, Object> checkout = capabilities.stream()
                        .filter(c -> "shopping.checkout".equals(c.get("name")))
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("Required UCP capability not found"));

                Map<String, Object> endpoints = (Map<String, Object>) checkout.get("endpoints");
                Map<String, Object> target = (Map<String, Object>) endpoints.get(intent);

                return (String) target.get("path");
            } catch (Exception e) {
                System.err.println("CRITICAL: UCP Manifest parsing failed for intent: " + intent);
                throw new RuntimeException("Protocol Mismatch: Unable to resolve UCP endpoint.");
            }
        }

        @Tool(description = "Step 1: Perform UCP Discovery and Create a checkout session based on merchant manifest.")
        public Map<String, Object> createUcpSession(String productId, int quantity) {
            String path = resolveEndpoint("create_session");

            System.out.println("LOG: UCP Discovery Successful. Resolved Path: " + path);

            return restClient.post()
                    .uri(path)
                    .body(Map.of("productId", productId, "quantity", quantity))
                    .retrieve()
                    .body(Map.class);
        }

        @Tool(description = "Step 2: Finalize a UCP session using the discovered 'complete_session' endpoint.")
        public Map<String, Object> completeUcpSession(String sessionId) {
            String pathTemplate = resolveEndpoint("complete_session");

            System.out.println("LOG: Executing UCP 'Complete' via discovered path: " + pathTemplate);

            return restClient.post()
                    .uri(pathTemplate, sessionId)
                    .retrieve()
                    .body(Map.class);
        }
    }
}
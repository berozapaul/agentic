package org.ai.agentic.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.ai.agentic.exceptions.ProductNotFoundException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class AgenticCommerceService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public Map<String, Object> initiateCheckout(Map<String, Object> cartRequest) {
        String userQuery = ((String) cartRequest.getOrDefault("productId", "")).toLowerCase();
        int quantity = (int) cartRequest.getOrDefault("quantity", 1);

        JsonNode product = findProductInCatalog(userQuery);

        double price = product.get("offers").get("price").asDouble();
        int totalAmountCents = (int) (price * 100 * quantity);
        String sessionId = "sess_" + UUID.randomUUID().toString().substring(0, 8);

        Map<String, Object> response = new HashMap<>();
        response.put("id", sessionId);
        response.put("total_amount", totalAmountCents);
        response.put("currency", "USD");

        // UCP State Machine Logic
        if (totalAmountCents < 10000) {
            response.put("status", "ready_for_complete");
        } else {
            response.put("status", "requires_escalation");
            response.put("reason", "High-value item (" + product.get("name").asText() + ") requires approval.");
            response.put("continue_url", "http://localhost:8080/checkout/resume/" + sessionId);
        }

        return response;
    }

    public Map<String, Object> finalizeOrder(String id) {
        return Map.of(
                "status", "completed",
                "order_id", "CONFIRMED-" + id.toUpperCase(),
                "timestamp", System.currentTimeMillis(),
                "message", "Order placed successfully via Agent."
        );
    }

    private JsonNode findProductInCatalog(String query) {
        try {
            InputStream is = new ClassPathResource("products.jsonld").getInputStream();
            JsonNode catalog = objectMapper.readTree(is).get("itemListElement");

            for (JsonNode item : catalog) {
                String name = item.get("name").asText().toLowerCase();
                String sku = item.get("sku").asText().toLowerCase();
                if (name.contains(query) || sku.contains(query)) {
                    return item;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Catalog access error");
        }
        throw new ProductNotFoundException("Product '" + query + "' not found in Schema.org catalog.");
    }
}

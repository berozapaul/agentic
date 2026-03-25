package org.ai.agentic.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/ucp")
public class AgenticCheckoutController {

    @PostMapping("/checkout-sessions")
    public ResponseEntity<Map<String, Object>> createSession(@RequestBody Map<String, Object> cartRequest) {
        String sessionId = "sess_" + UUID.randomUUID().toString().substring(0, 8);

        // UCP REQUIRED FIELDS
        Map<String, Object> response = new HashMap<>();
        response.put("id", sessionId);

        // Determine State:
        // 1. 'incomplete' = Agent needs to send more data (e.g. shipping address)
        // 2. 'requires_escalation' = Human must click a link (e.g. terms & conditions)
        // 3. 'ready_for_complete' = Agent can finish the buy right now

        response.put("status", "requires_escalation");
        response.put("total_amount", 15000); // UCP uses integers for currency (150.00)
        response.put("currency", "USD");

        // Handoff URL for escalation (Crucial for Demo!)
        response.put("continue_url", "http://localhost:8080/checkout/resume/" + sessionId);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/checkout-sessions/{id}/complete")
    public Map<String, Object> completeOrder(@PathVariable String id) {
        return Map.of(
                "status", "completed",
                "order_id", "CONFIRMED-" + id.toUpperCase(),
                "message", "Order placed successfully via Agent."
        );
    }
}
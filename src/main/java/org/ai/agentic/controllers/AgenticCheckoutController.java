package org.ai.agentic.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class AgenticCheckoutController {

    @PostMapping("/checkout")
    public ResponseEntity<Map<String, Object>> handleAgentCheckout(@RequestBody Map<String, Object> cartRequest) {
        // Log this so the audience sees the Agent's request!
        System.out.println("AI Agent is attempting to purchase: " + cartRequest);

        // Simulate creating a UCP Checkout Session
        String sessionId = "ucp_sess_" + System.currentTimeMillis();

        Map<String, Object> response = new HashMap<>();
        response.put("session_id", sessionId);
        response.put("status", "PAYMENT_REQUIRED"); // Tells the agent it needs to pay
        response.put("total_price", 150.00);
        response.put("currency", "USD");

        return ResponseEntity.ok(response);
    }
}

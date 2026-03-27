package org.ai.agentic.controllers;

import org.ai.agentic.exceptions.ProductNotFoundException;
import org.ai.agentic.services.AgenticCommerceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/ucp")
public class AgenticCheckoutController {

    private final AgenticCommerceService agenticCommerceService;

    public AgenticCheckoutController(AgenticCommerceService agenticCommerceService) {
        this.agenticCommerceService = agenticCommerceService;
    }

    @PostMapping("/checkout-sessions")
    public ResponseEntity<Map<String, Object>> createSession(@RequestBody Map<String, Object> cartRequest) {
        try {
            Map<String, Object> session = agenticCommerceService.initiateCheckout(cartRequest);
            return ResponseEntity.ok(session);
        } catch (ProductNotFoundException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/checkout-sessions/{id}/complete")
    public ResponseEntity<Map<String, Object>> completeOrder(@PathVariable String id) {
        return ResponseEntity.ok(agenticCommerceService.finalizeOrder(id));
    }
}
package org.ai.agentic.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class UcpDiscoveryController {

    @GetMapping("/.well-known/ucp")
    public Map<String, Object> getUcpManifest() {
        return Map.of(
                "ucp_version", "2026.1",
                "capabilities", List.of(
                        Map.of(
                                "name", "shopping.checkout",
                                "type", "agentic-commerce",
                                "version", "1.0",
                                "endpoints", Map.of(
                                        "create_session", Map.of(
                                                "path", "/api/v1/ucp/checkout-sessions",
                                                "method", "POST"
                                        ),
                                        "complete_session", Map.of(
                                                "path", "/api/v1/ucp/checkout-sessions/{id}/complete",
                                                "method", "POST"
                                        )
                                ),
                                "state_machine", Map.of(
                                        "states", List.of("incomplete", "ready_for_complete", "requires_escalation", "completed"),
                                        "final_state", "completed"
                                ),
                                "auth", Map.of(
                                        "type", "oauth2",
                                        "mode", "delegation",
                                        "scopes", List.of("purchase.on_behalf")
                                )
                        )
                )
        );
    }
}
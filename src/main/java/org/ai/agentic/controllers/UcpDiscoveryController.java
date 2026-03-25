package org.ai.agentic.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class UcpDiscoveryController {

    @GetMapping(value = "/.well-known/ucp", produces = "application/json")
    public Map<String, Object> getUcpManifest() {
        return Map.of(
                "ucp", Map.of(
                        "version", "2026-01-11",
                        "services", Map.of(
                                "dev.ucp.shopping", Map.of(
                                        "version", "2026-01-11",
                                        "rest", Map.of(
                                                "endpoint", "http://localhost:8080/api/v1/ucp"
                                        )
                                )
                        ),
                        "capabilities", List.of(
                                Map.of(
                                        "name", "dev.ucp.shopping.checkout",
                                        "version", "2026-01-11",
                                        "spec", "https://ucp.dev/specification/checkout"
                                )
                        )
                )
        );
    }
}
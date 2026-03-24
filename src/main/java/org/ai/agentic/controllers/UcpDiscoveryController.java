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
                "ucp_version", "2026-03-24",
                "capabilities", List.of(
                        Map.of(
                                "name", "dev.ucp.shopping.checkout",
                                "version", "2026-03-24",
                                "endpoint", "/api/v1/checkout"
                        )
                )
        );
    }
}

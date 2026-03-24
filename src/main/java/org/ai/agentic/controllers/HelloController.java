package org.ai.agentic.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/")
    public String index() {
        return "Spring Boot is officially running on my Mac!";
    }

    @GetMapping("/test")
    public String test() {
        return "The /test endpoint is also working!";
    }
}
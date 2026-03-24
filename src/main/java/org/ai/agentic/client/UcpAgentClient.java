package org.ai.agentic.client;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class UcpAgentClient {

    private static final String BASE_URL = "http://localhost:8080";

    public static void main(String[] args) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        Scanner scanner = new Scanner(System.in);

        System.out.println("🚀 [AGENT] Phase 1: Discovery...");
        HttpRequest discoveryRequest = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/.well-known/ucp"))
                .GET()
                .build();

        HttpResponse<String> discoveryResponse = client.send(discoveryRequest, HttpResponse.BodyHandlers.ofString());
        System.out.println("✅ [AGENT] Found Manifest: " + discoveryResponse.body());

        System.out.println("\n🤔 [AGENT] Should I proceed with the checkout? (yes/no)");
        if (scanner.nextLine().equalsIgnoreCase("yes")) {

            System.out.println("🛒 [AGENT] Phase 2: Initiating Agentic Checkout...");

            // This JSON mimics the UCP "Intent" payload
            String cartJson = """
                {
                    "items": [{"id": "laptop_99", "qty": 1}],
                    "agent_id": "java-agent-001",
                    "payment_mode": "deferred"
                }
                """;

            HttpRequest checkoutRequest = HttpRequest.newBuilder()
                    .uri(URI.create(BASE_URL + "/api/v1/checkout"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(cartJson))
                    .build();

            HttpResponse<String> checkoutResponse = client.send(checkoutRequest, HttpResponse.BodyHandlers.ofString());

            System.out.println("🎊 [AGENT] Transaction Result: " + checkoutResponse.body());
        } else {
            System.out.println("❌ [AGENT] Checkout cancelled by user.");
        }
    }
}
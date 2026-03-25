package org.ai.agentic.client;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class UcpAgentClient {

    private static final String BASE_URL = "http://localhost:8080/api/v1/ucp";
    private static final String DISCOVERY_URL = "http://localhost:8080/.well-known/ucp";

    public static void main(String[] args) throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        Scanner scanner = new Scanner(System.in);

        // --- STEP 1: DISCOVERY ---
        System.out.println("🔍 [AGENT] Phase 1: UCP Discovery Handshake...");
        HttpRequest discoveryReq = HttpRequest.newBuilder().uri(URI.create(DISCOVERY_URL)).GET().build();
        String manifest = client.send(discoveryReq, HttpResponse.BodyHandlers.ofString()).body();
        System.out.println("✅ [AGENT] Capabilities Found: " + manifest);

        // --- STEP 2: INITIATE SESSION ---
        System.out.println("\n🛒 [AGENT] Phase 2: Creating Checkout Session...");
        String cartJson = """
            {
                "items": [{"id": "laptop_99", "qty": 1}],
                "context": {"agent_type": "autonomous-procurement"}
            }
            """;

        HttpRequest createReq = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/checkout-sessions"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(cartJson))
                .build();

        String sessionResponse = client.send(createReq, HttpResponse.BodyHandlers.ofString()).body();
        System.out.println("📄 [AGENT] Session Initialized: " + sessionResponse);

        // --- STEP 3: STATE-BASED LOGIC (The "Agentic" Part) ---
        // In a real demo, use a JSON library (Jackson/Gson) to parse these.
        // For simplicity, we'll check the raw string:

        if (sessionResponse.contains("requires_escalation")) {
            System.out.println("\n⚠️ [AGENT] Action Needed: Human intervention required.");
            System.out.println("🔗 [AGENT] Please visit this URL to continue: http://localhost:8080/resume-checkout");
        }
        else if (sessionResponse.contains("ready_for_complete")) {
            System.out.println("\n🤖 [AGENT] Analysis: Session is valid and total is verified.");
            System.out.print("🤔 [USER] Authorize agent to finalize purchase? (yes/no): ");

            if (scanner.nextLine().equalsIgnoreCase("yes")) {
                // Extract session ID (Simulated extraction for demo)
                String sessionId = "sess_demo_123";

                // --- STEP 4: COMPLETION ---
                System.out.println("⚡ [AGENT] Phase 3: Finalizing Transaction...");
                HttpRequest completeReq = HttpRequest.newBuilder()
                        .uri(URI.create(BASE_URL + "/checkout-sessions/" + sessionId + "/complete"))
                        .POST(HttpRequest.BodyPublishers.noBody())
                        .build();

                String finalResult = client.send(completeReq, HttpResponse.BodyHandlers.ofString()).body();
                System.out.println("🎊 [AGENT] DONE! Merchant Response: " + finalResult);
            } else {
                System.out.println("🛑 [AGENT] Transaction aborted by user.");
            }
        }
    }
}
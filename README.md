# 🤖 UCP Agentic Commerce: Autonomous Procurement Node

This repository contains a reference implementation of an **Autonomous Commerce Agent** built using **Spring AI** and **Google Gemini 2.5**. The project demonstrates the **Universal Commerce Protocol (UCP) v2026.1**, allowing AI agents to perform secure, delegated transactions on behalf of human users.

---

## 🌟 Executive Summary

In the 2026 Agentic Economy, this project moves beyond simple chatbots. It showcases a **Procurement Agent** that:
1.  **Discovers** merchant capabilities via a standardized `/.well-known/ucp` manifest.
2.  **Identifies** products using Schema.org structured data (`products.jsonld`).
3.  **Executes** purchases autonomously within pre-defined governance limits.
4.  **Escalates** high-value transactions to humans via secure Handoff URLs.

---

## 🏗️ Technical Architecture

The system follows a clean, three-tier architecture:

* **The Brain (Gemini 2.5):** Uses a sophisticated "Executive Presence" system prompt to manage intent and protocol reasoning.
* **The Discovery Tool (UcpTools):** Implements "Late Binding," resolving API endpoints at runtime via the UCP Manifest to ensure zero hardcoding.
* **The Commerce Core (AgenticCommerceService):** A headless commerce engine that manages the UCP state machine.

---

## 🚀 Getting Started

### 1. Prerequisites
* **Java 17+** and **Maven 3.8+**
* **Gemini API Key:** Obtain one from Google AI Studio.

### 2. Set Environment Variable
Before running the app, set your API key in your terminal.

**Windows (PowerShell):**
$env:GOOGLE_AI_GEMINI_API_KEY='your_api_key_here'

**Mac/Linux:**
export GOOGLE_AI_GEMINI_API_KEY='your_api_key_here'

### 3. Build and Launch
Run the following command in the project root:
mvn clean spring-boot:run

### 4. Access the Terminal
Open your browser to: http://localhost:8080

---

## 🧪 Demo Scenarios to Try

| User Prompt | System Logic | Expected Outcome |
| :--- | :--- | :--- |
| **"I want to buy a mouse."** | Price < $100 | **Autonomous Success:** The agent creates the session and completes it instantly. |
| **"Buy a Pro Laptop"** | Price > $100 | **Governance Escalation:** The agent pauses and provides a clickable **Approval Link**. |
| **"What can I buy for $50?"** | Catalog Lookup | **Search:** The agent uses the Schema.org catalog to find eligible SKUs. |

---

## 📂 Key File Map

| File | Responsibility |
| :--- | :--- |
| `products.jsonld` | **Source of Truth:** Schema.org compliant product catalog. |
| `AiConfig.java` | **Agent Tools:** Logic for UCP Discovery and Session Management. |
| `AgenticCommerceService.java` | **Business Logic:** State machine and price-based governance. |
| `UcpDiscoveryController.java` | **Protocol Entry:** Serves the `/.well-known/ucp` manifest. |
| `index.html` | **Executive UI:** Professional terminal with Markdown/Table support. |

---

## 🛡️ Security & Compliance
* **OAuth 2.0 Delegation:** All agent calls simulate a delegated user token.
* **Auditability:** Every step of the Agent's reasoning is logged to the server console.
* **Zero-Trust Discovery:** The agent verifies the merchant's manifest before sending any transaction data.

---
*Developed for the 2026 Agentic Commerce Showcase.*
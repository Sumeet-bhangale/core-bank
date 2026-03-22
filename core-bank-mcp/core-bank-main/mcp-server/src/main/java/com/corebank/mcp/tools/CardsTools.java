package com.corebank.mcp.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

/**
 * MCP tools for the Cards microservice (port 9000).
 * Exposes create, fetch, update, delete operations on credit cards.
 */
@Component
public class CardsTools {

    private final RestClient restClient;

    public CardsTools(@Qualifier("cardsRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    @Tool(description = "Create a new Credit Card for a customer using their 10-digit mobile number. Default card limit is 100000. One card per mobile number.")
    public String createCard(String mobileNumber) {
        try {
            return restClient.post()
                    .uri("/api/create?mobileNumber={mobile}", mobileNumber)
                    .retrieve()
                    .body(String.class);
        } catch (Exception e) {
            return "Error creating card: " + e.getMessage();
        }
    }

    @Tool(description = "Fetch credit card details for a customer by 10-digit mobile number. Returns card number, type, total limit, amount used, and available amount.")
    public String fetchCard(String mobileNumber) {
        try {
            return restClient.get()
                    .uri("/api/fetch?mobileNumber={mobile}", mobileNumber)
                    .retrieve()
                    .body(String.class);
        } catch (Exception e) {
            return "Error fetching card: " + e.getMessage();
        }
    }

    @Tool(description = "Update card details. Provide: mobileNumber (10 digits), cardNumber (12 digits), cardType (e.g. 'Credit Card'), totalLimit, amountUsed, availableAmount.")
    public String updateCard(String mobileNumber, String cardNumber, String cardType,
                             int totalLimit, int amountUsed, int availableAmount) {
        try {
            Map<String, Object> body = Map.of(
                    "mobileNumber", mobileNumber,
                    "cardNumber", cardNumber,
                    "cardType", cardType,
                    "totalLimit", totalLimit,
                    "amountUsed", amountUsed,
                    "availableAmount", availableAmount
            );
            return restClient.put()
                    .uri("/api/update")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .body(String.class);
        } catch (Exception e) {
            return "Error updating card: " + e.getMessage();
        }
    }

    @Tool(description = "Delete the credit card associated with the given 10-digit mobile number.")
    public String deleteCard(String mobileNumber) {
        try {
            return restClient.delete()
                    .uri("/api/delete?mobileNumber={mobile}", mobileNumber)
                    .retrieve()
                    .body(String.class);
        } catch (Exception e) {
            return "Error deleting card: " + e.getMessage();
        }
    }
}

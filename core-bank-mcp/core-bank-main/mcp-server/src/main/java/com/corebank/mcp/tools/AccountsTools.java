package com.corebank.mcp.tools;

import java.util.Map;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/**
 * MCP tools for the Accounts microservice (port 8080).
 * Exposes create, fetch, update, delete operations on customer accounts.
 */
@Component
public class AccountsTools {

    private final RestClient restClient;

    public AccountsTools(@Qualifier("accountsRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    @Tool(description = "Create a new bank account for a customer. Requires: name (5–50 chars), email (valid email), mobileNumber (exactly 10 digits). Returns a status message.")
    public String createAccount(String name, String email, String mobileNumber) {
        try {
            return restClient.post()
                    .uri("/api/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Map.of("name", name, "email", email, "mobileNumber", mobileNumber))
                    .retrieve()
                    .body(String.class);
        } catch (Exception e) {
            return "Error creating account: " + e.getMessage();
        }
    }

    @Tool(description = "Fetch account and customer details using a 10-digit mobile number. Returns customer name, email, account number, account type, and branch address.")
    public String fetchAccount(String mobileNumber) {
        try {
            return restClient.get()
                    .uri("/api/fetch?mobileNumber={mobile}", mobileNumber)
                    .retrieve()
                    .body(String.class);
        } catch (Exception e) {
            return "Error fetching account: " + e.getMessage();
        }
    }

    @Tool(description = "Update a bank account. Provide: name, email, mobileNumber, accountNumber (numeric, from fetchAccount), accountType ('Savings' or 'Current'), branchAddress, govtId (Aadhaar/PAN/Passport).")
    public String updateAccount(String name, String email, String mobileNumber,
                                Long accountNumber, String accountType, String branchAddress, String govtId) {
        try {
            Map<String, Object> body = Map.of(
                    "name", name,
                    "email", email,
                    "mobileNumber", mobileNumber,
                    "accountsDto", Map.of(
                            "accountNumber", accountNumber,
                            "accountType", accountType,
                            "branchAddress", branchAddress,
                            "govtId", govtId
                    )
            );
            return restClient.put()
                    .uri("/api/update")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .body(String.class);
        } catch (Exception e) {
            return "Error updating account: " + e.getMessage();
        }
    }

    @Tool(description = "Delete the bank account (and customer record) for the given 10-digit mobile number.")
    public String deleteAccount(String mobileNumber) {
        try {
            return restClient.delete()
                    .uri("/api/delete?mobileNumber={mobile}", mobileNumber)
                    .retrieve()
                    .body(String.class);
        } catch (Exception e) {
            return "Error deleting account: " + e.getMessage();
        }
    }
}

package com.corebank.mcp.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Map;

/**
 * MCP tools for the Loans microservice (port 8090).
 * Exposes create, fetch, update, delete operations on home loans.
 */
@Component
public class LoansTools {

    private final RestClient restClient;

    public LoansTools(@Qualifier("loansRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    @Tool(description = "Create a new Home Loan for a customer using their 10-digit mobile number. Default loan amount is 100000. One loan per mobile number.")
    public String createLoan(String mobileNumber) {
        try {
            return restClient.post()
                    .uri("/api/create?mobileNumber={mobile}", mobileNumber)
                    .retrieve()
                    .body(String.class);
        } catch (Exception e) {
            return "Error creating loan: " + e.getMessage();
        }
    }

    @Tool(description = "Fetch loan details for a customer by 10-digit mobile number. Returns loan number, type, total loan, amount paid, and outstanding amount.")
    public String fetchLoan(String mobileNumber) {
        try {
            return restClient.get()
                    .uri("/api/fetch?mobileNumber={mobile}", mobileNumber)
                    .retrieve()
                    .body(String.class);
        } catch (Exception e) {
            return "Error fetching loan: " + e.getMessage();
        }
    }

    @Tool(description = "Update loan details. Provide: mobileNumber (10 digits), loanNumber (12 digits), loanType (e.g. 'Home Loan'), totalLoan, amountPaid, outstandingAmount.")
    public String updateLoan(String mobileNumber, String loanNumber, String loanType,
                             int totalLoan, int amountPaid, int outstandingAmount) {
        try {
            Map<String, Object> body = Map.of(
                    "mobileNumber", mobileNumber,
                    "loanNumber", loanNumber,
                    "loanType", loanType,
                    "totalLoan", totalLoan,
                    "amountPaid", amountPaid,
                    "outstandingAmount", outstandingAmount
            );
            return restClient.put()
                    .uri("/api/update")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .body(String.class);
        } catch (Exception e) {
            return "Error updating loan: " + e.getMessage();
        }
    }

    @Tool(description = "Delete the loan associated with the given 10-digit mobile number.")
    public String deleteLoan(String mobileNumber) {
        try {
            return restClient.delete()
                    .uri("/api/delete?mobileNumber={mobile}", mobileNumber)
                    .retrieve()
                    .body(String.class);
        } catch (Exception e) {
            return "Error deleting loan: " + e.getMessage();
        }
    }
}

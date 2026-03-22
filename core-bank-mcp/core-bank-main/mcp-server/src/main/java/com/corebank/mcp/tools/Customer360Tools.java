package com.corebank.mcp.tools;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/**
 * MCP tool that aggregates data from all 3 CoreBank services
 * to provide a complete customer view in a single tool call.
 */
@Component
public class Customer360Tools {

    private final RestClient accountsClient;
    private final RestClient cardsClient;
    private final RestClient loansClient;

    public Customer360Tools(
            @Qualifier("accountsRestClient") RestClient accountsClient,
            @Qualifier("cardsRestClient") RestClient cardsClient,
            @Qualifier("loansRestClient") RestClient loansClient) {
        this.accountsClient = accountsClient;
        this.cardsClient = cardsClient;
        this.loansClient = loansClient;
    }

    @Tool(description = "Get a complete 360-degree view of a customer by 10-digit mobile number. Aggregates account details (from port 8080), card details (port 9000), and loan details (port 8090) in one response. Returns partial data if a service is unavailable.")
    public String getCustomer360(String mobileNumber) {
        StringBuilder view = new StringBuilder();
        view.append("=== Customer 360 View: ").append(mobileNumber).append(" ===\n\n");

        // Account info (accounts service - port 8080)
        try {
            String accountData = accountsClient.get()
                    .uri("/api/fetch?mobileNumber={m}", mobileNumber)
                    .retrieve()
                    .body(String.class);
            view.append("[ACCOUNT - port 8080]\n").append(accountData).append("\n\n");
        } catch (Exception e) {
            view.append("[ACCOUNT - port 8080] Unavailable: ").append(e.getMessage()).append("\n\n");
        }

        // Card info (cards service - port 9000)
        try {
            String cardData = cardsClient.get()
                    .uri("/api/fetch?mobileNumber={m}", mobileNumber)
                    .retrieve()
                    .body(String.class);
            view.append("[CARD - port 9001]\n").append(cardData).append("\n\n");
        } catch (Exception e) {
            view.append("[CARD - port 9001] Unavailable: ").append(e.getMessage()).append("\n\n");
        }

        // Loan info (loans service - port 8090)
        try {
            String loanData = loansClient.get()
                    .uri("/api/fetch?mobileNumber={m}", mobileNumber)
                    .retrieve()
                    .body(String.class);
            view.append("[LOAN - port 8090]\n").append(loanData).append("\n\n");
        } catch (Exception e) {
            view.append("[LOAN - port 8090] Unavailable: ").append(e.getMessage()).append("\n\n");
        }

        return view.toString();
    }
}

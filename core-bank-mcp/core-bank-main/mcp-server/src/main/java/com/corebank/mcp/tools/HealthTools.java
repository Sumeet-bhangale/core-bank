package com.corebank.mcp.tools;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

/**
 * MCP tool that checks the Spring Actuator health of all 3 CoreBank services.
 */
@Component
public class HealthTools {

    private final RestClient accountsClient;
    private final RestClient cardsClient;
    private final RestClient loansClient;

    public HealthTools(
            @Qualifier("accountsRestClient") RestClient accountsClient,
            @Qualifier("cardsRestClient") RestClient cardsClient,
            @Qualifier("loansRestClient") RestClient loansClient) {
        this.accountsClient = accountsClient;
        this.cardsClient = cardsClient;
        this.loansClient = loansClient;
    }

    @Tool(description = "Check the health status of all CoreBank microservices by calling their Spring Actuator /actuator/health endpoints. Returns UP or DOWN with details for each service.")
    public String getAllServicesHealth() {
        Map<String, String> statuses = new LinkedHashMap<>();
        statuses.put("accounts (http://localhost:8080)", probeHealth(accountsClient));
        statuses.put("cards    (http://localhost:9001)", probeHealth(cardsClient));
        statuses.put("loans    (http://localhost:8090)", probeHealth(loansClient));

        StringBuilder sb = new StringBuilder("=== CoreBank Services Health ===\n");
        statuses.forEach((service, status) ->
                sb.append("  ").append(service).append("  →  ").append(status).append("\n")
        );
        return sb.toString();
    }

    private String probeHealth(RestClient client) {
        try {
            String response = client.get()
                    .uri("/actuator/health")
                    .retrieve()
                    .body(String.class);
            return "UP  " + response;
        } catch (Exception e) {
            return "DOWN  (" + e.getMessage() + ")";
        }
    }
}

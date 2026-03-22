package com.corebank.mcp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

/**
 * Creates one RestClient bean per CoreBank microservice, each pre-configured
 * with the target service's base URL from application.yml.
 */
@Configuration
public class RestClientConfig {

    @Value("${corebank.services.accounts-url}")
    private String accountsUrl;

    @Value("${corebank.services.cards-url}")
    private String cardsUrl;

    @Value("${corebank.services.loans-url}")
    private String loansUrl;

    @Bean("accountsRestClient")
    public RestClient accountsRestClient() {
        return RestClient.builder().baseUrl(accountsUrl).build();
    }

    @Bean("cardsRestClient")
    public RestClient cardsRestClient() {
        return RestClient.builder().baseUrl(cardsUrl).build();
    }

    @Bean("loansRestClient")
    public RestClient loansRestClient() {
        return RestClient.builder().baseUrl(loansUrl).build();
    }
}

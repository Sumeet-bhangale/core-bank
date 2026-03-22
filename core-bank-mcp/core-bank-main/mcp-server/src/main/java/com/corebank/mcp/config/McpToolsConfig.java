package com.corebank.mcp.config;

import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.corebank.mcp.tools.AccountsTools;
import com.corebank.mcp.tools.CardsTools;
import com.corebank.mcp.tools.Customer360Tools;
import com.corebank.mcp.tools.HealthTools;
import com.corebank.mcp.tools.LoansTools;

/**
 * Registers all @Tool-annotated methods from the tools package
 * as MCP tools, so AI clients (GitHub Copilot, Claude, etc.)
 * can discover and invoke them.
 */
@Configuration
public class McpToolsConfig {

    @Bean
    public ToolCallbackProvider coreBankToolCallbackProvider(
            AccountsTools accountsTools,
            CardsTools cardsTools,
            LoansTools loansTools,
            Customer360Tools customer360Tools,
            HealthTools healthTools) {

        return MethodToolCallbackProvider.builder()
                .toolObjects(accountsTools, cardsTools, loansTools, customer360Tools, healthTools)
                .build();
    }
}

package org.example.service;

import io.modelcontextprotocol.server.McpServerFeatures;
import io.modelcontextprotocol.server.McpSyncServer;
import io.modelcontextprotocol.server.McpSyncServerExchange;
import io.modelcontextprotocol.spec.McpSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Map;
import java.util.function.BiFunction;

@Configuration
public class McpServiceRegistry {

    @Autowired
    private McpSyncServer mcpSyncServer;

    public void registerTool(String name, String description, String inputJsonSchema, String outputContent) {
        var mcpTool = new McpSchema.Tool(name, description, inputJsonSchema);

        BiFunction<McpSyncServerExchange, Map<String, Object>, McpSchema.CallToolResult> toolFunction = (exchange, invocation) -> McpSchema.CallToolResult.builder()
                .isError(false)
                .addTextContent(outputContent)
                .build();

        var newTool = new McpServerFeatures.SyncToolSpecification(mcpTool, toolFunction);

        mcpSyncServer.addTool(newTool);

        System.out.println("Registered new tool: " + name);
    }
}

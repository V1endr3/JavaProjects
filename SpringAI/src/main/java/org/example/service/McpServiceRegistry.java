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
        /**
         * create definition
         *
         * name: key,
         * description: description,
         * inputJsonSchema: {
         *     "type": "object",
         *     "properties": {
         *         "path": {},
         *         "query": {},
         *         "body": {}
         *     }
         * }
         */
        var mcpTool = new McpSchema.Tool(name, description, inputJsonSchema);

        /**
         * process the tool invocation
         *
         * McpSyncServerExchange => xxx
         * Map<String, Object> invocation => AI根据JSON Schema生成的参数 {"path": {}, "query": {}, "body": {}}
         * McpSchema.CallToolResult => 返回结果类型
         */
        BiFunction<McpSyncServerExchange, Map<String, Object>, McpSchema.CallToolResult> toolFunction =
                (exchange, invocation) -> {
            // todo:
            // RestClient做接口请求, 用 invocation 中的参数进行填充
                    Object body = invocation.get("body");
                    Object result = null;
                    return McpSchema.CallToolResult.builder()
                            .isError(false)
                            .addTextContent(outputContent)
                            .build();
        };

        var newTool = new McpServerFeatures.SyncToolSpecification(mcpTool, toolFunction);

        mcpSyncServer.addTool(newTool);

        System.out.println("Registered new tool: " + name);
    }
}

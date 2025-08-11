package org.example.controller;

import org.example.service.McpServiceRegistry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TemplateController {

    @Autowired
    private McpServiceRegistry mcpServiceRegistry;

    @PostMapping("/registerTool")
    public String registerTool(@RequestParam("name") String name,
                               @RequestParam("description")String description,
                               @RequestParam("inputJsonSchema")String inputJsonSchema,
                               @RequestParam("outputContent")String outputContent) {
        mcpServiceRegistry.registerTool(name, description, inputJsonSchema, outputContent);
        return "Tool registered successfully: " + name;
    }
}

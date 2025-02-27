package org.example.runner.controller;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dsl.xml.io.XmlRoutesBuilderLoader;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.spi.Resource;
import org.apache.camel.spi.RoutesLoader;
import org.apache.camel.support.ResourceHelper;
import org.example.runner.model.JobDefinition;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class RunnerController {
    private final CamelContext camelContext = new DefaultCamelContext();

    @PostMapping("/run")
    public Object execute(@RequestBody JobDefinition jobDefinition) {
        Resource xmlResource = ResourceHelper.fromBytes("xml", jobDefinition.getXmlContent().getBytes());
        try (XmlRoutesBuilderLoader xmlRoutesBuilderLoader = new XmlRoutesBuilderLoader()) {
            RoutesLoader routeBuilderLoader = camelContext.getCamelContextExtension().getContextPlugin(RoutesLoader.class);
            RouteBuilder xmlRouteBuilder = xmlRoutesBuilderLoader.doLoadRouteBuilder(xmlResource);
            camelContext.addRoutes(xmlRouteBuilder);
            camelContext.start();

            ProducerTemplate template = camelContext.createProducerTemplate();
            template.sendBody("direct:mock-start", jobDefinition.getStartContent());

            camelContext.getRouteController().stopRoute("");
            camelContext.removeRoute("");
            return "执行完成";
        } catch (Exception e) {
            log.error("执行失败", e);
            return "执行失败";
        }
    }
}

package org.example.runner;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Endpoint;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.Route;
import org.apache.camel.component.rest.RestEndpoint;
import org.apache.camel.main.KameletMain;
import org.apache.camel.spi.CamelEvent;
import org.apache.camel.spi.RouteController;
import org.apache.camel.support.EventNotifierSupport;
import org.apache.camel.support.ScheduledPollEndpoint;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

@Slf4j
public class DemoRunner {
    public static void main(String[] args) throws Exception {
        String filepath = "file:Camel/conf/test.xml";
        boolean hasRest = false;
        Endpoint restEndpoint = null;
        // initialize main
        KameletMain main = new KameletMain("orchestration");
        main.configure().withRoutesIncludePattern(filepath);
        main.start();

        RouteController routeController = main.getCamelContext().getRouteController();
        routeController.stopAllRoutes();
        List<Route> routeList = main.getCamelContext().getRoutes();
        for (Route route : routeList) {
            Endpoint startPoint = route.getEndpoint();
            if (startPoint instanceof ScheduledPollEndpoint) {
                ((ScheduledPollEndpoint) startPoint).setRepeatCount(1);
                String scheduler = (String) ((ScheduledPollEndpoint) startPoint).getScheduler();
                if ("none".equals(scheduler)) {
                    ((ScheduledPollEndpoint) startPoint).setDelay(0);
                    ((ScheduledPollEndpoint) startPoint).setInitialDelay(0);
                } else if ("quartz".equals(scheduler)) {
                    ((ScheduledPollEndpoint) startPoint).setScheduler("quartz");
                    Map<String, Object> props = new HashMap<>();
                    props.put("cron", "0/1 * * * * ?");
                    ((ScheduledPollEndpoint) startPoint).setSchedulerProperties(props);
                } else {
                    throw new IllegalArgumentException("不支持的调度类型: " + scheduler);
                }
            } else if (startPoint instanceof RestEndpoint) {
                restEndpoint = startPoint;
                hasRest = true;
                log.info("test");
            } else {
                log.error("不支持的试运行类型：{}", startPoint.getClass().getName());
                throw new IllegalArgumentException("不支持的试运行类型");
            }
        }
        routeController.startAllRoutes();

        CountDownLatch latch = new CountDownLatch(1);
        main.getCamelContext().getManagementStrategy().addEventNotifier(new EventNotifierSupport() {
            @Override
            public void notify(CamelEvent event) throws Exception {
                if (event instanceof CamelEvent.ExchangeCompletedEvent) {
                    latch.countDown();
                }
            }
        });

        if (hasRest) {
            ProducerTemplate producerTemplate = main.getCamelContext().createProducerTemplate();
            producerTemplate.sendBody(restEndpoint, "hello");
        }

        latch.await();
        main.stop();
    }
}

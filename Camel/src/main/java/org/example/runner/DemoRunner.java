package org.example.runner;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DemoRunner {
    public static void main(String[] args) {
        String location = "route1";

        String xmlContent = "<route id=\"route1\"><from uri=\"direct:mock-start\"/><to uri=\"log:end\"/></route>";
        
        System.out.println(xmlContent);
    }
}

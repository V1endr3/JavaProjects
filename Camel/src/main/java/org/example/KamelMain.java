package org.example;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.main.KameletMain;

@Slf4j
public class KamelMain {
    public static void main(String[] args) throws Exception {
        KameletMain main = new KameletMain("orchestration");
        main.configure().withRoutesIncludePattern("file:Camel/conf/test.xml");
        main.run(args);
        
        log.info("Endpoints size: {}", main.getCamelContext().getEndpoints().size());
    }
}

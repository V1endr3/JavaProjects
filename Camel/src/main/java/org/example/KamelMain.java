package org.example;

import org.apache.camel.main.KameletMain;

public class KamelMain {
    public static void main(String[] args) throws Exception {
        KameletMain main = new KameletMain("orchestration");
        main.configure().withRoutesIncludePattern("file:Camel/conf/test.xml");
        main.run(args);
    }
}

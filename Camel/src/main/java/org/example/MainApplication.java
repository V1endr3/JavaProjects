package org.example;

import org.apache.camel.spring.Main;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class MainApplication {
    public static void main(String[] args) throws Exception {
        Main main = new Main();
        FileSystemXmlApplicationContext applicationContext = new FileSystemXmlApplicationContext("file:Camel/conf/test.xml");
        main.setApplicationContext(applicationContext);
        main.run();
    }
}
package org.example;

import org.apache.camel.spring.Main;

public class MainApplication {
    public static void main(String[] args) throws Exception {
        String[] argList = {"-fa", "file:Camel/conf/test.xml"};
        Main main = new Main();
        main.run(argList);
    }
}
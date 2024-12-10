package org.example;

import org.apache.camel.main.Main;

public class CamelMain {
    public static void main(String[] args) {
        try {
            Main main = new Main(CamelMain.class);
            main.configure().withRoutesIncludePattern("file:Camel/conf/test.xml");
            main.run(args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
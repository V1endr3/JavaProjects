package org.example;

import com.mzt.logapi.starter.annotation.EnableLogRecord;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableLogRecord(tenant = "org.example")
@MapperScan("org.example")
@SpringBootApplication
public class MainApplication {

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

}
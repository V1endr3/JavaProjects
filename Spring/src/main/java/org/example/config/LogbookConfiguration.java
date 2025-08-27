package org.example.config;

import org.example.common.LogbookSinkWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.zalando.logbook.Logbook;

@Lazy
@Configuration
public class LogbookConfiguration {

    @Autowired
    private LogbookSinkWriter sinkWriter;

    @Bean
    public Logbook logbook() {
        return Logbook.builder()
                .sink(sinkWriter)
                .build();
    }

}

package org.example.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnBean(name = "firstImplService")
public class RelayOnFirstConfig {

    @Value("${custom.config}")
    private String config;

    @Bean
    public String relayConfig() {
        return "here is relay config";
    }
}

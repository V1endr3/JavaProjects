package org.example.service.impl;

import org.example.service.AnyService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

@Service
@ConditionalOnProperty(value = "provider.runner", havingValue = "second")
public class SecondImplService implements AnyService {

    @Value("${provider.host}")
    private String host;

    @Value("${provider.port}")
    private Integer port;

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public String getAddress() {
        return host + port;
    }

}

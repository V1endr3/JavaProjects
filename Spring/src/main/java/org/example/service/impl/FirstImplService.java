package org.example.service.impl;

import jakarta.annotation.PostConstruct;
import org.example.service.AnyService;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

// first or default
@Service
@ConditionalOnProperty(value = "provider.runner", havingValue = "first", matchIfMissing = true)
public class FirstImplService implements AnyService {

    @Value("${provider.address:localhost:8081}")
    private String address;

    private String finalAddress;

    @Autowired
    private BeanFactory beanFactory;

    @PostConstruct
    public void init() {
        this.finalAddress = address;
    }

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public String getAddress() {
        String extraInfo = "";
        if (beanFactory.containsBean("relayConfig")) {
            String relayConfig = beanFactory.getBean("relayConfig", String.class);
            extraInfo = " - " + relayConfig;
        }
        return finalAddress + extraInfo;
    }


}

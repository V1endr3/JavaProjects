package org.example.config;

import com.github.f4b6a3.uuid.UuidCreator;
import com.mybatisflex.core.keygen.IKeyGenerator;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
public class UUIDKeyGenerator implements IKeyGenerator {
    @Override
    public Object generate(Object o, String s) {
        return LocalDateTime.now().getNano();
    }

    public static void main(String[] args) {
        UUID timeOrdered = UuidCreator.getTimeOrderedEpoch();
        System.out.println(timeOrdered);
    }
}

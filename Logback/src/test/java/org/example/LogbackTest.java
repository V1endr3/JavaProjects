package org.example;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class LogbackTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(LogbackTest.class);

    @Test
    public void testLoggerIdentifier() {
        Logger instance0001 = LoggerFactory.getLogger("unique0001");
        Logger instance0002 = LoggerFactory.getLogger("unique0001");
        // getLogger(name) will get the same instance
        Assertions.assertEquals(instance0001, instance0002);
    }

    @Test
    public void testParameterizedLogging() {
        List<String> dataList = new ArrayList<>();
        dataList.add("First");
        dataList.add("Second");

        for (int index = 0; index < dataList.size(); index++) {
            // reduce the cost of parameter construction
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Index: {}, Content: {}", index, dataList.get(index));
            }
        }
    }

    @Test
    public void testThrowError() {
        Assertions.assertThrows(IllegalStateException.class, () -> {
            Exception someError = new IllegalStateException("some error");
            LOGGER.error("error occurred, ", someError);
            throw someError;
        });
    }
}

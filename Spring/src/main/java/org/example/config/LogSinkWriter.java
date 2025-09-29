package org.example.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zalando.logbook.*;
import org.zalando.logbook.core.ExtendedLogFormatSink;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class LogSinkWriter implements Sink {

    @Override
    public void write(Precorrelation precorrelation, HttpRequest request) {
        // no implements for this method
    }

    @Override
    public void write(Correlation correlation, HttpRequest request, HttpResponse response) throws IOException {
        byte[] requestBody = request.getBody();
        byte[] responseBody = response.getBody();

        System.out.println();
    }
}

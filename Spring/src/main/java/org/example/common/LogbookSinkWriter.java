package org.example.common;

import org.springframework.stereotype.Component;
import org.zalando.logbook.*;

import java.io.IOException;

@Component
public class LogbookSinkWriter implements Sink {
    @Override
    public void write(Precorrelation precorrelation, HttpRequest request) {
        System.out.println(precorrelation);
        System.out.println(request);
    }

    @Override
    public void write(Correlation correlation, HttpRequest request, HttpResponse response) {
        System.out.println(correlation);
        System.out.println(request);
    }

    @Override
    public void writeBoth(final Correlation correlation, final HttpRequest request, final HttpResponse response) throws IOException {
        this.write(correlation, request);
        this.write(correlation, request, response);
    }
}

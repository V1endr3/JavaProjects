package org.example.debugger;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class DebuggerProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        var headers = exchange.getIn().getHeaders();
        return;
    }
}
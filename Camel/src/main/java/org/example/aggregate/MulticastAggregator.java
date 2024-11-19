package org.example.aggregate;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;

public class MulticastAggregator implements AggregationStrategy {
    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        var exchangeIndex = newExchange.getProperty("CamelMulticastIndex");
        return null;
    }
}

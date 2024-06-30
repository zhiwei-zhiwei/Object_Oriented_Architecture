package Imp;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

import java.util.ArrayList;
import java.util.List;

public class StandardDeviationStrategy implements AggregationStrategy {
    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        ArrayList<String> list;
        String newBody = newExchange.getIn().getBody(String.class);

        if (oldExchange == null) {
            list = new ArrayList<>();
            list.add(newBody);
            newExchange.getIn().setBody(list);
            return newExchange;
        } else {
            list = oldExchange.getIn().getBody(ArrayList.class);
            list.add(newBody);
            return oldExchange;
        }
    }

    public static double calculateStandardDeviation(ArrayList<Double> data) {
        if (data.size() <= 1) return 0.0;
        double mean = data.stream().mapToDouble(d -> d).average().orElse(0.0);
        double variance = data.stream().mapToDouble(d -> (d - mean) * (d - mean)).average().orElse(0.0);
        return Math.sqrt(variance);
    }
}
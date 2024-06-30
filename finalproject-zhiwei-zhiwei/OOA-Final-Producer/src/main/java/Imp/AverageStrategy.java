package Imp;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;

import java.util.ArrayList;

public class AverageStrategy implements AggregationStrategy {
    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        String newBody = newExchange.getIn().getBody(String.class);
        ArrayList<String> list = null;
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

    public static double calculateAverage(double value, int size) {
        return value / size;
    }
}

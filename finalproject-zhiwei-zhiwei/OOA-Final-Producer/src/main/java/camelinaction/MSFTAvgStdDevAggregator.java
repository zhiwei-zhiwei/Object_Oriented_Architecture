package camelinaction;

import Imp.ReportEngine;
import Imp.StandardDeviationStrategy;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.impl.DefaultCamelContext;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.component.timer.TimerComponent;

import java.util.ArrayList;

import Imp.AverageStrategy;

public class MSFTAvgStdDevAggregator {

    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();

        ((PropertiesComponent) context.getComponent("properties")).setLocation("classpath:application.properties");

        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        context.addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));

        TimerComponent timer = new TimerComponent();
        context.addComponent("timer", timer);

        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() {
                from("timer://pollingTimer?period={{polling.period}}")
                        .log("Polling for new data...")
                        .to("file:/Users/zhiweicao/Documents/A-Zhiwei/UChicago/Spring2024/OOA/Project/OOA-Final-Producer/data/inbox?noop=true")
                        .split().tokenize("\n")
                        .process(exchange -> {
                            String body = exchange.getIn().getBody(String.class);
                            String modifiedBody = body.replace("[", "").replace("]", "").replace("\t", " ");
                            exchange.getIn().setBody(modifiedBody);
                        })
                        .to("jms:queue:StockQueue")
                        .log("Polled and sent new data to JMS queue: ${body}");

                from("jms:queue:MSFTStockQueue")
                        .wireTap("direct:standardDeviation")
                        .to("direct:average");

                from("direct:average")
                        .log("Received message for aggregation: ${body}")
                        .aggregate(simple("${body.split(' ')[0]}"), new AverageStrategy())
                        .completionInterval(5000)
                        .process(exchange -> {
                            ArrayList<String> aggregated = exchange.getIn().getBody(ArrayList.class);
                            double bid_price_sum = 0;
                            double bid_quantity_sum = 0;
                            double ask_price_sum = 0;
                            double ask_quantity_sum = 0;

                            for (String message : aggregated) {
                                String[] parts = message.split(" ");
                                double bidPrice = Double.parseDouble(parts[1]);
                                double bidQuantity = Double.parseDouble(parts[2]);
                                double askPrice = Double.parseDouble(parts[3]);
                                double askQuantity = Double.parseDouble(parts[4]);
                                bid_price_sum += bidPrice;
                                bid_quantity_sum += bidQuantity;
                                ask_price_sum += askPrice;
                                ask_quantity_sum += askQuantity;
                            }

                            int size = aggregated.size();
                            double bid_price_average = AverageStrategy.calculateAverage(bid_price_sum, size);
                            double bid_quantity_average = AverageStrategy.calculateAverage(bid_quantity_sum, size);
                            double ask_price_average = AverageStrategy.calculateAverage(ask_price_sum, size);
                            double ask_quantity_average = AverageStrategy.calculateAverage(ask_quantity_sum, size);
                            String ticker = aggregated.get(0).split(" ")[0];
                            String result = "Ticker: " + ticker + ", Moving Average Bid Price: " + bid_price_average + ", Moving Average Bid Quantity: " + bid_quantity_average + ", Moving Average Ask Price: " + ask_price_average + ", Moving Average Ask Quantity: " + ask_quantity_average;
                            exchange.getIn().setBody(result);
                            ReportEngine.getInstance().reportStatistics(result);
                        })
                        .to("jms:queue:MSFTAvgAggregatedStockQuotes")
                        .log("Aggregated Message Sent: ${body}");

                from("direct:standardDeviation")
                        .aggregate(simple("${body.split(' ')[0]}"), new StandardDeviationStrategy())
                        .completionInterval(5000)
                        .process(exchange -> {
                            ArrayList<String> aggregated = exchange.getIn().getBody(ArrayList.class);
                            ArrayList<Double> bid_prices = new ArrayList<>();
                            ArrayList<Double> bid_quantities = new ArrayList<>();
                            ArrayList<Double> ask_prices = new ArrayList<>();
                            ArrayList<Double> ask_quantities = new ArrayList<>();

                            for (String message : aggregated) {
                                String[] parts = message.split(" ");
                                bid_prices.add(Double.parseDouble(parts[1]));
                                bid_quantities.add(Double.parseDouble(parts[2]));
                                ask_prices.add(Double.parseDouble(parts[3]));
                                ask_quantities.add(Double.parseDouble(parts[4]));
                            }

                            double bid_price_stdDev = StandardDeviationStrategy.calculateStandardDeviation(bid_prices);
                            double bid_quantity_stdDev = StandardDeviationStrategy.calculateStandardDeviation(bid_quantities);
                            double ask_price_stdDev = StandardDeviationStrategy.calculateStandardDeviation(ask_prices);
                            double ask_quantity_stdDev = StandardDeviationStrategy.calculateStandardDeviation(ask_quantities);
                            String ticker = aggregated.get(0).split(" ")[0];

                            String result = String.format("Ticker: %s, Std Dev Bid Price: %.4f, Std Dev Bid Quantity: %.4f, Std Dev Ask Price: %.4f, Std Dev Ask Quantity: %.4f",
                                    ticker, bid_price_stdDev, bid_quantity_stdDev, ask_price_stdDev, ask_quantity_stdDev);
                            exchange.getIn().setBody(result);
                            ReportEngine.getInstance().reportStatistics(result);
                        })
                        .to("jms:queue:MSFTStdDevAggregatedStockQuotes")
                        .log("Aggregated Message Sent: ${body}");
            }
        });

        context.start();
        System.out.println("Trading engine is now consuming messages from the queue...");
        Thread.sleep(60000);
        context.stop();
    }
}
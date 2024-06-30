package camelinaction;

import Imp.AverageTradingEngine;
import Imp.StdDevTradingEngine;
import Imp.TradingEngine;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.component.jms.JmsComponent;

public class TradingEngineObserver {

    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();

        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        context.addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));

        TradingEngine avgTradingEngine = new AverageTradingEngine();
        TradingEngine stdDevTradingEngine = new StdDevTradingEngine();

        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() {
                from("jms:queue:AvgAggregatedStockQuotes")
                        .process(exchange -> {
                            String data = exchange.getIn().getBody(String.class);
                            avgTradingEngine.onDataUpdate(data);
                        })
                        .log("Average data processed: ${body}");

                from("jms:queue:StdDevAggregatedStockQuotes")
                        .process(exchange -> {
                            String data = exchange.getIn().getBody(String.class);
                            stdDevTradingEngine.onDataUpdate(data);
                        })
                        .log("Standard deviation data processed: ${body}");
            }
        });

        context.start();

        Thread.sleep(20000);

        context.stop();
    }
}
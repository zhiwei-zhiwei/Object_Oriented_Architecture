package camelinaction;

import Imp.Portfolio;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.component.jms.JmsComponent;

public class TestCamel {

    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();

        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        context.addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));

        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() {
                from("jms:queue:PortfolioQueue")
                        .process(exchange -> {
                            String json = exchange.getIn().getBody(String.class);
                            ObjectMapper mapper = new ObjectMapper();
                            Portfolio mainPortfolio = mapper.readValue(json, Portfolio.class);
                            exchange.getIn().setBody(mainPortfolio);
                        })
                        .to("jms:queue:ConsumerPortfolioQueue")
                        .log("Received portfolio from queue: ${body}");
            }
        });

        context.start();

        Thread.sleep(60000);

        context.stop();
    }
}
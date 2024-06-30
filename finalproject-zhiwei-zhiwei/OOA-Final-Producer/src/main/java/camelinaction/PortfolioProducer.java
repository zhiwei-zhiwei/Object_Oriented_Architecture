package camelinaction;

import Imp.Portfolio;
import Imp.PortfolioComponent;
import Imp.Stock;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.Exchange;

import java.util.Scanner;

public class PortfolioProducer {

    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();

        ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
        context.addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));

        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() {
                from("direct:start")
                        .process(exchange -> {
                            Portfolio mainPortfolio = createPortfolioFromUserInput();
                            sendStocksToQueues(mainPortfolio, exchange);
                        });
            }
        });

        context.start();

        Scanner scanner = new Scanner(System.in);
        System.out.println("Press Enter to start sending portfolios...");
        scanner.nextLine();
        context.createProducerTemplate().sendBody("direct:start", "");

        context.stop();
    }

    private static Portfolio createPortfolioFromUserInput() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the name of the main portfolio: ");
        String mainPortfolioName = scanner.nextLine();
        Portfolio mainPortfolio = new Portfolio(mainPortfolioName);

        boolean addingStocks = true;
        while (addingStocks) {
            System.out.print("Enter the name of the stock: ");
            String stockName = scanner.nextLine();

            System.out.print("Enter the bid price: ");
            double bidPrice = scanner.nextDouble();

            System.out.print("Enter the bid quantity: ");
            double bidQuantity = scanner.nextDouble();

            System.out.print("Enter the ask price: ");
            double askPrice = scanner.nextDouble();

            System.out.print("Enter the ask quantity: ");
            double askQuantity = scanner.nextDouble();

            scanner.nextLine();

            Stock stock = new Stock(stockName, bidPrice, bidQuantity, askPrice, askQuantity);
            mainPortfolio.add(stock);

            System.out.print("Do you want to add another stock? (yes/no): ");
            String response = scanner.nextLine();
            if (response.equalsIgnoreCase("no")) {
                addingStocks = false;
            }
        }

        return mainPortfolio;
    }

    private static void sendStocksToQueues(Portfolio portfolio, Exchange exchange) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        for (PortfolioComponent component : portfolio.getComponents()) {
            if (component instanceof Stock) {
                String stockJson = mapper.writeValueAsString(component);
                String queueName = "jms:queue:" + portfolio.getName().replaceAll(" ", "_") + "_Queue";
                exchange.getContext().createProducerTemplate().sendBody(queueName, stockJson);
            } else if (component instanceof Portfolio) {
                sendStocksToQueues((Portfolio) component, exchange);
            }
        }
    }
}
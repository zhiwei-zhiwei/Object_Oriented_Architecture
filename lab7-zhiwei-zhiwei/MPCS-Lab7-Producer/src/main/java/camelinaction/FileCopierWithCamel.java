/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package camelinaction;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.CamelContext;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.component.jms.JmsComponent;
import javax.jms.ConnectionFactory;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.dataformat.rss.RssDataFormat;


public class FileCopierWithCamel {

    public static void main(String[] args) {
        CamelContext context = new DefaultCamelContext();

        try {
            // Connection setup for ActiveMQ
            ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
            context.addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));

            context.addRoutes(new RouteBuilder() {
                public void configure() {
                    String googleNewsURL = "https://news.google.com/?output=rss";
//                    from("rss:" + googleNewsURL)
//                        .setBody().xpath("/rss/channel/item/title/text()")
//                        .to("log:Received Message?showAll=true");
                    from("rss:" + googleNewsURL)
//                    .setBody().xpath("/rss/channel/item/title/text()", String.class)
                    .process(exchange -> {
                    	String rssContent = exchange.getIn().getBody(String.class);
                        int lastStartTitle = rssContent.lastIndexOf("<title>") + "<title>".length();
                        int lastEndTitle = rssContent.indexOf("</title>", lastStartTitle);
                        String fullTitle = rssContent.substring(lastStartTitle, lastEndTitle).trim();

                        int dashIndex = fullTitle.lastIndexOf(" - ");
                        String title = (dashIndex != -1) ? fullTitle.substring(0, dashIndex) : fullTitle;
                        String source = (dashIndex != -1) ? fullTitle.substring(dashIndex + 3) : "Unknown source";

                        exchange.getIn().setHeader("Title", title);
                        exchange.getIn().setHeader("Source", source);
                    })
                    
                    // uncomment the code from line 62 - 69 and comment line 71 - 82 to test the final requirement
                    
//                    .filter().simple("${in.header.Title} regex '.*(?i)(COVID|gay marriage|Obama).*'")
//                    .process(exchange -> {
//                        String title = exchange.getIn().getHeader("Title", String.class);
//                        String source = exchange.getIn().getHeader("Source", String.class);
//                        String formattedOutput = String.format("<title=\"%s\" source=\"%s\"/>", title, source);
//                        exchange.getIn().setBody(formattedOutput);
//                    })
//                    .to("jms:queue:RSS_GOOGLE_NEWS_UPDATES");
                    
                    .log("Processed message for title: ${header.Title} and source: ${header.Source}")
                    .split().tokenize(",").parallelProcessing()
                        .choice()
                            .when(header("CamelSplitIndex").isEqualTo(0))
                                .setBody(header("Title"))
                                .to("jms:queue:RSS_GOOGLE_NEWS_UPDATES")
                            .when(header("CamelSplitIndex").isEqualTo(1))
                                .setBody(header("Source"))
                                .to("jms:queue:RSS_GOOGLE_NEWS_UPDATES")
                    .end()
                    .to("file:data/outbox?fileName=messages.log&fileExist=Append");
                }
            });

            // Start the route and let it do its work
            context.start();
            Thread.sleep(10000);  // let the application have some time to process messages

            // Stop the CamelContext
            context.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
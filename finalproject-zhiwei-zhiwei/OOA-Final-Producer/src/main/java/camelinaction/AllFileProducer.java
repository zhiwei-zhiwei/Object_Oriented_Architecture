/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package camelinaction;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.component.jms.JmsComponent;

public class AllFileProducer {

    public static void main(String args[]) throws Exception {
        CamelContext context = new DefaultCamelContext();

        ConnectionFactory connectionFactory =
                new ActiveMQConnectionFactory("tcp://localhost:61616");
        context.addComponent("jms",
                JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));

        context.addRoutes(new RouteBuilder() {
            public void configure() {
                from("file:/Users/zhiweicao/Documents/A-Zhiwei/UChicago/Spring2024/OOA/Project/OOA-Final-Producer/data/inbox?noop=true&delay=1000")
                        .log("RETRIEVED: ${file:name}")
                        .unmarshal().csv()
                        .split(body())
                        .process(exchange -> {
                            String body = exchange.getIn().getBody(String.class);
                            String modifiedBody = body.replace("[", "").replace("]", "").replace("\t", " ");
                            exchange.getIn().setBody(modifiedBody);
                        })
                        .to("jms:queue:StockQueue");
            }
        });

        context.start();
        Thread.sleep(20000);

        context.stop();
    }
}

package com.example.query;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaStreamsAnalytics {

    @Bean
    public KTable<String, Double> totalSalesTable(StreamsBuilder builder) {

        KStream<String, Order> orderStream =
                builder.stream("orders-topic",
                        Consumed.with(Serdes.String(), new OrderSerde()));

        KTable<String, Double> totalSales =
                orderStream
                        .mapValues(Order::getAmount)
                        .groupBy((key, value) -> "total",
                                Grouped.with(Serdes.String(), Serdes.Double()))
                        .reduce(Double::sum);

        totalSales.toStream()
                .to("total-sales-topic",
                        Produced.with(Serdes.String(), Serdes.Double()));

        return totalSales;
    }
}
@Bean
public KTable<String, Double> productSalesTable(StreamsBuilder builder) {

    KStream<String, Order> orderStream =
            builder.stream("orders-topic",
                    Consumed.with(Serdes.String(), new OrderSerde()));

    KTable<String, Double> productSales =
            orderStream
                    .groupBy((key, order) -> order.getProduct(),
                            Grouped.with(Serdes.String(), new OrderSerde()))
                    .aggregate(
                            () -> 0.0,
                            (product, order, total) -> total + order.getAmount(),
                            Materialized.with(Serdes.String(), Serdes.Double())
                    );

    productSales.toStream()
            .to("product-sales-topic",
                    Produced.with(Serdes.String(), Serdes.Double()));

    return productSales;
}
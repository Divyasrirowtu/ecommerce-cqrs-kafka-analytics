package com.example.query;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.state.Materialized;
import org.apache.kafka.streams.StreamsBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class KafkaStreamsTopology {

    @Bean
    public KStream<String, JsonNode> kStream(StreamsBuilder builder) {

        // 1️⃣ Create KTable for products
        KTable<String, JsonNode> productsTable = builder.table(
            "product-events",
            Materialized.as("product-store")
        );

        // 2️⃣ Create KStream for orders
        KStream<String, JsonNode> ordersStream = builder.stream("order-events");

        // 3️⃣ Enrich orders with product info (simplified example)
        KStream<String, JsonNode> enrichedOrders = ordersStream.mapValues(order -> {
            // join logic with productsTable would go here
            return order;
        });

        // 4️⃣ Product sales aggregation
        enrichedOrders
            .flatMap((key, order) -> {
                List<KeyValue<String, Double>> result = new ArrayList<>();
                for (JsonNode item : order.get("items")) {
                    String productId = item.get("productId").asText();
                    double value = item.get("quantity").asDouble() * item.get("price").asDouble();
                    result.add(KeyValue.pair(productId, value));
                }
                return result;
            })
            .groupByKey()
            .reduce(
                (agg, value) -> agg + value,
                Materialized.as("product-sales-store")
            );

        // 5️⃣ Category revenue aggregation
        enrichedOrders
            .flatMap((key, order) -> {
                List<KeyValue<String, Double>> result = new ArrayList<>();
                for (JsonNode item : order.get("items")) {
                    String category = item.get("category").asText();
                    double value = item.get("quantity").asDouble() * item.get("price").asDouble();
                    result.add(KeyValue.pair(category, value));
                }
                return result;
            })
            .groupByKey()
            .reduce(
                (agg, value) -> agg + value,
                Materialized.as("category-revenue-store")
            );

        // 6️⃣ Hourly sales aggregation (1-hour tumbling window)
        enrichedOrders
            .flatMap((key, order) -> {
                List<KeyValue<String, Double>> result = new ArrayList<>();
                for (JsonNode item : order.get("items")) {
                    double value = item.get("quantity").asDouble() * item.get("price").asDouble();
                    result.add(KeyValue.pair("hourly", value));
                }
                return result;
            })
            .groupByKey()
            .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofHours(1)))
            .reduce(
                (agg, value) -> agg + value,
                Materialized.as("hourly-sales-store")
            );

        return enrichedOrders;
    }
}
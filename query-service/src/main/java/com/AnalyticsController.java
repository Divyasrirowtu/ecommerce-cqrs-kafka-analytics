private final KTable<Windowed<String>, Double> lastFiveMinSales;

public AnalyticsController(KTable<String, Double> totalSalesTable,
                           KTable<String, Double> productSalesTable,
                           KTable<String, String> topProductTable,
                           KTable<Windowed<String>, Double> lastFiveMinSales) {

    this.totalSalesTable = totalSalesTable;
    this.productSalesTable = productSalesTable;
    this.topProductTable = topProductTable;
    this.lastFiveMinSales = lastFiveMinSales;
}

@GetMapping("/last-5-min-sales")
public Double getLastFiveMinSales() {

    ReadOnlyWindowStore<String, Double> store =
            (ReadOnlyWindowStore<String, Double>)
                    lastFiveMinSales.queryableStoreName();

    Instant now = Instant.now();
    Instant fiveMinAgo = now.minus(Duration.ofMinutes(5));

    try (KeyValueIterator<Windowed<String>, Double> iterator =
                 lastFiveMinSales.toStream().groupByKey().windowedBy(
                         TimeWindows.ofSizeWithNoGrace(Duration.ofMinutes(5))
                 ).reduce(Double::sum).toStream().toTable()
                 .queryableStoreName()) {

        Double total = 0.0;
        while (iterator.hasNext()) {
            total += iterator.next().value;
        }
        return total;
    }
}
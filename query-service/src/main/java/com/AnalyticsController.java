private final KTable<String, String> topProductTable;

public AnalyticsController(KTable<String, Double> totalSalesTable,
                           KTable<String, Double> productSalesTable,
                           KTable<String, String> topProductTable) {
    this.totalSalesTable = totalSalesTable;
    this.productSalesTable = productSalesTable;
    this.topProductTable = topProductTable;
}

@GetMapping("/top-product")
public String getTopProduct() {
    return topProductTable.get("top");
}
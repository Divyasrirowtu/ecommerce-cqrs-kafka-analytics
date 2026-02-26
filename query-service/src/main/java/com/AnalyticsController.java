@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

    private final KTable<String, Double> totalSalesTable;
    private final KTable<String, Double> productSalesTable;

    public AnalyticsController(KTable<String, Double> totalSalesTable,
                               KTable<String, Double> productSalesTable) {
        this.totalSalesTable = totalSalesTable;
        this.productSalesTable = productSalesTable;
    }

    @GetMapping("/total-sales")
    public Double getTotalSales() {
        return totalSalesTable.get("total");
    }

    @GetMapping("/product-sales/{product}")
    public Double getProductSales(@PathVariable String product) {
        return productSalesTable.get(product);
    }
}

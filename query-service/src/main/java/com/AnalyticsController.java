package com.example.query;

import org.apache.kafka.streams.kstream.KTable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController {

    private final KTable<String, Double> totalSalesTable;

    public AnalyticsController(KTable<String, Double> totalSalesTable) {
        this.totalSalesTable = totalSalesTable;
    }

    @GetMapping("/total-sales")
    public Double getTotalSales() {
        return totalSalesTable.get("total");
    }
}

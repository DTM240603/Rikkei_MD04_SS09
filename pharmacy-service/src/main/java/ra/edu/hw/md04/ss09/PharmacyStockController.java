package ra.edu.hw.md04.ss09;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/pharmacy")
public class PharmacyStockController {

    private final WarehouseClient warehouseClient;

    public PharmacyStockController(WarehouseClient warehouseClient) {
        this.warehouseClient = warehouseClient;
    }

    @GetMapping("/{medicineId}/stock")
    public WarehouseClient.WarehouseStockResponse checkStock(@PathVariable String medicineId) {
        return warehouseClient.checkStock(medicineId);
    }
}

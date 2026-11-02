package ra.edu.hw.md04.ss09;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/warehouse")
public class WarehouseController {

    @GetMapping("/{medicineId}/stock")
    public StockResponse checkStock(@PathVariable String medicineId) {
        return new StockResponse(medicineId, true, 100);
    }

    public record StockResponse(String medicineId, boolean available, int quantity) {
    }
}

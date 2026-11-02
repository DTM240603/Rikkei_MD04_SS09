package ra.edu.hw.md04.ss09;

import java.math.BigDecimal;
import java.math.RoundingMode;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RefreshScope
@RestController
@RequestMapping("/api/v1/bill")
public class BillController {

    private final BigDecimal vatRate;

    public BillController(@Value("${pharmacy.vat-rate}") BigDecimal vatRate) {
        this.vatRate = vatRate;
    }

    @PostMapping
    public BillResponse calculateBill(@RequestBody BillRequest request) {
        if (request == null || request.medicineTotal() == null
                || request.medicineTotal().signum() < 0) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "medicineTotal must be zero or greater");
        }

        BigDecimal medicineTotal = request.medicineTotal().setScale(2, RoundingMode.HALF_UP);
        BigDecimal vatAmount = medicineTotal.multiply(vatRate)
                .movePointLeft(2)
                .setScale(2, RoundingMode.HALF_UP);

        return new BillResponse(
                medicineTotal,
                vatRate,
                vatAmount,
                medicineTotal.add(vatAmount).setScale(2, RoundingMode.HALF_UP));
    }

    public record BillRequest(BigDecimal medicineTotal) {
    }

    public record BillResponse(
            BigDecimal medicineTotal,
            BigDecimal vatRate,
            BigDecimal vatAmount,
            BigDecimal totalAmount) {
    }
}

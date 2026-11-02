package ra.edu.hw.md04.ss09;

import java.net.http.HttpClient;
import java.time.Duration;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.server.ResponseStatusException;

@Service
public class WarehouseClient {

    private final RestClient restClient;

    public WarehouseClient(
            @Value("${warehouse.service.url:http://localhost:8081}") String warehouseServiceUrl,
            @Value("${warehouse.service.connect-timeout:2s}") Duration connectTimeout,
            @Value("${warehouse.service.read-timeout:3s}") Duration readTimeout) {
        HttpClient httpClient = HttpClient.newBuilder()
                .connectTimeout(connectTimeout)
                .build();
        JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory(httpClient);
        requestFactory.setReadTimeout(readTimeout);

        this.restClient = RestClient.builder()
                .baseUrl(warehouseServiceUrl)
                .requestFactory(requestFactory)
                .build();
    }

    @CircuitBreaker(name = "warehouseCB", fallbackMethod = "warehouseUnavailable")
    public WarehouseStockResponse checkStock(String medicineId) {
        return restClient.get()
                .uri("/api/v1/warehouse/{medicineId}/stock", medicineId)
                .retrieve()
                .body(WarehouseStockResponse.class);
    }

    private WarehouseStockResponse warehouseUnavailable(String medicineId, Exception exception) {
        throw new ResponseStatusException(
                HttpStatus.SERVICE_UNAVAILABLE,
                "Warehouse service is unavailable; try again later",
                exception);
    }

    public record WarehouseStockResponse(String medicineId, boolean available, int quantity) {
    }
}

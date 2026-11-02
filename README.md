# Centralized pharmacy configuration

The Config Server reads `pharmacy-service.properties` from
`https://github.com/DTM240603/Rikkei_MD04_SS09_Config.git` and serves it on
port `8888`. The pharmacy service requires the Config Server at startup.

## Run locally

From the project directory, start the Config Server:

```powershell
cd config-server
.\gradlew.bat bootRun
```

In another terminal, start the pharmacy service:

```powershell
cd pharmacy-service
.\gradlew.bat bootRun
```

The Git repository can be overridden with the `CONFIG_REPO_URI` environment
variable. PostgreSQL defaults to
`jdbc:postgresql://localhost:5432/pharmacy_db` with user `postgres`; override
`PHARMACY_DB_URL`, `PHARMACY_DB_USERNAME`, and `PHARMACY_DB_PASSWORD` as needed.
The password is not committed; set `PHARMACY_DB_PASSWORD` in the pharmacy
service environment when the database requires authentication.

## Refresh VAT without restarting

Set `pharmacy.vat-rate` (as a percentage, for example `10`) in
`pharmacy-service.properties` in the Git configuration repository. After
committing and pushing the change, refresh the running pharmacy service:

```powershell
Invoke-RestMethod -Method Post -Uri http://localhost:8080/actuator/refresh
```

Calculate a bill by sending the medicine subtotal:

```powershell
Invoke-RestMethod -Method Post -Uri http://localhost:8080/api/v1/bill `
  -ContentType "application/json" -Body '{"medicineTotal":100000}'
```

The response includes the medicine subtotal, VAT rate, VAT amount, and total.

## Warehouse Circuit Breaker

The warehouse service is stateless and does not require a database for this
exercise. It serves a sample stock response on port `8081`; pharmacy-service
calls it through the `warehouseCB` circuit breaker. Start it in a separate
terminal:

```powershell
cd warehouse-service
.\gradlew.bat bootRun
```

In the configuration Git repository, `pharmacy-service.properties` sets the
circuit breaker to open after at least four calls when at least 50% fail, then
wait 20 seconds before allowing two trial calls. Commit and push those settings
to GitHub, then refresh pharmacy-service with `POST /actuator/refresh` so the
running application receives them.

Check stock through pharmacy-service:

```powershell
Invoke-RestMethod -Uri http://localhost:8080/api/v1/pharmacy/ASPIRIN/stock
```

To try fail-fast behavior, stop warehouse-service and call the pharmacy stock
endpoint four times. Once the breaker opens, further calls return HTTP 503
without waiting for warehouse-service. It remains open for 20 seconds before
trial calls are permitted.

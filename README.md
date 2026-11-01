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

# Warehouse

## How to run locally

1. Build Spring Boot jar and Docker image out of it with `mvn clean install`
2. Run `docker-compose.yaml`. Database schema will be initialized on start-up from `schema.sql`.

## API

### Open API
Available at `http://localhost:8081/v3/api-docs`

### Swagger UI
Available at `http://localhost:8081/swagger-ui.html`

### Functional requirements

- Upload articles: `curl -F 'file=@data/inventory.json' 'http://localhost:8081/inventory'`

- Upload products: `curl -F 'file=@data/products.json' 'http://localhost:8081/products'`

- Sell product: `curl -iS -H "Content-Type: application/json" 'http://localhost:8081/products/checkout' --data '{"name": "Dinning Table", "amount": "1"}'`

- List available products: `curl -iS 'http://localhost:8081/products-available'`

## Notes

### Data access

While ORM solutions (e.g. JPA, Hibernate) when setup and used correctly could often make developer's life easier, here it's been decided to go with Spring JDBC without entity mapping to reduce overhead and keep things simpler. Hence, all data manipulations are implemented using almost pure SQL and constraints were enforced in the schema, rather than on application level.

### Metrics

Prometheus metrics are available at `http://localhost:8088/manage`.
List of metrics (set by `management.endpoints.web.exposure.include` property):
- `/info`
- `/health`
- `/prometheus`
- `/metrics`

Also, custom metric `products.sold` was added to track total number of sold products.
It could be found under `http://localhost:8088/manage/prometheus`.
The implementation for tracking this metric is based on Spring Aspects, see: `com.test.warehouse.service.metrics.ProductMetricsAspect.java`


### Tests

Tests were implemented for service and DAO layers. Service layer tests use mocks for DAO layer. However, in our case there's not much value in service tests, because we almost don't have any business logic to test, i.e. we mostly test what we mocked.

For DAO layer integration tests it's quite common to use in-memory database, e.g.`H2`, instead of a real one. Such databases are usually lightweight, have less overhead and allow to run tests quickly and efficiently. They generally work well with ORM frameworks, such as Hibernate. `H2` was our first choice too. However, since we're not using ORM and rely on a schema and queries for MySQL, there were issues with different dialects between these two DBs. That's why it's been decided to use test-container solution, which deploys a real separate database (MySQL in our case) in a Docker container and tests run against it.   
# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Test Commands

```bash
# Build
./gradlew build

# Run unit tests (excludes integration tests)
./gradlew test

# Run integration tests (requires Docker for Testcontainers)
./gradlew integrationTest

# Run a single test class
./gradlew test --tests "TopicServiceTest"

# Run a single test method
./gradlew test --tests "TopicServiceTest.listTopics_shouldReturnSortedTopics"

# Run the application locally
./gradlew bootRun

# Run with custom Kafka
KAFKA_BOOTSTRAP_SERVERS=kafka:9092 ./gradlew bootRun
```

## Architecture

This is a **read-only REST API** for Kafka cluster administration built with Spring Boot 4 and Java 21.

### Hexagonal Architecture (Ports & Adapters)

```
Controller (REST) → Service (Business Logic) → KafkaAdminPort (Interface) → KafkaAdminClientAdapter (Kafka AdminClient)
```

- **KafkaAdminPort** (`common/KafkaAdminPort.java`): Port interface abstracting all Kafka operations. Services depend on this interface, not the concrete adapter.
- **KafkaAdminClientAdapter** (`common/KafkaAdminClientAdapter.java`): Implements the port using Kafka's AdminClient. Handles exception translation to domain exceptions.

### Package-by-Feature Structure

Each feature (`topic/`, `consumergroup/`, `cluster/`) contains:
- Domain models (Java records)
- DTOs with `from()` factory methods for conversion
- Service layer (sorting, transformation, delegation to port)
- REST controller
- Feature-specific exception (e.g., `TopicNotFoundException`)

### Testing Strategy

- **Unit tests**: Mock the port interface in service tests, mock services in controller tests
- **Integration tests**: Tagged with `@Tag("integration")`, use Testcontainers with real Kafka
- Base class: `BaseIntegrationTest` provides shared Kafka container setup

## Key Configuration

- Virtual threads enabled (`spring.threads.virtual.enabled: true`)
- Environment variable `KAFKA_BOOTSTRAP_SERVERS` overrides Kafka connection
- OpenAPI docs at `/swagger-ui.html` and `/api-docs`
- Prometheus metrics at `/actuator/prometheus`

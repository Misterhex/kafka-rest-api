# Kafka Admin REST API

A REST API for Kafka cluster administration, providing read-only operations to inspect topics, consumer groups, and cluster metadata.

## Tech Stack

- **Java 21**
- **Spring Boot 3.2.1**
- **Apache Kafka Client 4.0.0**
- **SpringDoc OpenAPI** for API documentation
- **Micrometer + Prometheus** for metrics
- **Testcontainers** for integration testing

## Current Phase

This API currently supports **read-only operations** for inspecting Kafka cluster state.

---

## API Endpoints

### Topics (`/api/v1/topics`)

| Method | Path | Description |
|--------|------|-------------|
| GET | `/api/v1/topics` | List all topics |
| GET | `/api/v1/topics/{name}` | Get topic details |
| GET | `/api/v1/topics/{name}/partitions` | Get partition info |

### Consumer Groups (`/api/v1/consumer-groups`)

| Method | Path | Description |
|--------|------|-------------|
| GET | `/api/v1/consumer-groups` | List all consumer groups |
| GET | `/api/v1/consumer-groups/{groupId}` | Get consumer group details |
| GET | `/api/v1/consumer-groups/{groupId}/offsets` | Get offsets and lag |

### Cluster (`/api/v1/cluster`)

| Method | Path | Description |
|--------|------|-------------|
| GET | `/api/v1/cluster` | Get cluster info |
| GET | `/api/v1/cluster/brokers` | List all brokers |
| GET | `/api/v1/cluster/brokers/{id}` | Get broker details |

### Other Endpoints

| Path | Description |
|------|-------------|
| `/swagger-ui.html` | Swagger UI |
| `/api-docs` | OpenAPI specification |
| `/actuator/health` | Health check |
| `/actuator/prometheus` | Prometheus metrics |

---

## Codebase Structure

The project follows a **package-by-feature** architecture:

```
src/main/java/com/kafkaadmin/
├── KafkaAdminApiApplication.java       # Application entry point
├── common/                              # Shared components
│   ├── KafkaAdminPort.java             # Port interface for Kafka operations
│   ├── KafkaAdminClientAdapter.java    # Kafka AdminClient adapter
│   ├── KafkaAdminClientConfig.java     # AdminClient bean configuration
│   ├── KafkaAdminProperties.java       # Configuration properties
│   ├── KafkaAdminException.java        # Base exception class
│   ├── ErrorResponse.java              # Error response DTO
│   └── GlobalExceptionHandler.java     # Global exception handling
├── topic/                               # Topic feature
│   ├── Topic.java                      # Domain model
│   ├── TopicPartitionInfo.java         # Partition model
│   ├── TopicDto.java                   # List response DTO
│   ├── TopicDetailDto.java             # Detail response DTO
│   ├── TopicPartitionInfoDto.java      # Partition DTO
│   ├── TopicService.java               # Business logic
│   ├── TopicController.java            # REST controller
│   └── TopicNotFoundException.java     # Exception
├── consumergroup/                       # Consumer group feature
│   ├── ConsumerGroup.java              # Domain model
│   ├── ConsumerGroupMember.java        # Member model
│   ├── ConsumerGroupOffset.java        # Offset model
│   ├── ConsumerGroupDto.java           # List response DTO
│   ├── ConsumerGroupDetailDto.java     # Detail response DTO
│   ├── ConsumerGroupMemberDto.java     # Member DTO
│   ├── ConsumerGroupOffsetDto.java     # Offset DTO
│   ├── ConsumerGroupService.java       # Business logic
│   ├── ConsumerGroupController.java    # REST controller
│   └── ConsumerGroupNotFoundException.java
└── cluster/                             # Cluster feature
    ├── Broker.java                     # Broker model
    ├── ClusterInfo.java                # Cluster info model
    ├── BrokerDto.java                  # Broker list DTO
    ├── BrokerDetailDto.java            # Broker detail DTO
    ├── ClusterInfoDto.java             # Cluster info DTO
    ├── ClusterService.java             # Business logic
    ├── ClusterController.java          # REST controller
    └── BrokerNotFoundException.java    # Exception
```

---

## Getting Started

### Prerequisites

- Java 21
- Docker (for local Kafka or running with docker-compose)

### Quick Start

**Option 1: Using Docker Compose (recommended)**

```bash
# Start Kafka and the API (builds automatically)
docker-compose up -d

# API available at http://localhost:8080
# Kafka UI available at http://localhost:8081
```

**Option 2: Run locally with external Kafka**

```bash
# Build the application
./gradlew build

# Run with default settings (connects to localhost:9092)
./gradlew bootRun

# Or run with custom Kafka bootstrap servers
KAFKA_BOOTSTRAP_SERVERS=kafka1:9092,kafka2:9092 ./gradlew bootRun
```

---

## Development

### Build

```bash
./gradlew build
```

### Run Tests

```bash
# Run unit tests
./gradlew test

# Run integration tests (requires Docker for Testcontainers)
./gradlew integrationTest
```

### Run Locally

```bash
# Start only Kafka
docker-compose up -d kafka

# Run the application
./gradlew bootRun
```

---

## Configuration

### Key Properties

| Property | Default | Description |
|----------|---------|-------------|
| `kafka.admin.bootstrap-servers` | `localhost:9092` | Kafka bootstrap servers |
| `kafka.admin.request-timeout-ms` | `30000` | Request timeout in milliseconds |
| `kafka.admin.default-api-timeout-ms` | `60000` | Default API timeout in milliseconds |
| `kafka.admin.client-id` | `kafka-admin-api` | Client ID for Kafka connections |
| `server.port` | `8080` | Server port |

### Environment Variables

Configuration can be overridden using environment variables:

| Environment Variable | Property |
|---------------------|----------|
| `KAFKA_BOOTSTRAP_SERVERS` | `kafka.admin.bootstrap-servers` |
| `SPRING_PROFILES_ACTIVE` | Active Spring profile |

---

## License

This project is licensed under the [Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0).

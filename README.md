# Kafka Admin REST API

A REST API for Kafka cluster administration, providing read-only operations to inspect topics, consumer groups, and cluster metadata.

## Tech Stack

- **Java 21**
- **Spring Boot 4.0.1**
- **Apache Kafka Client** (managed by Spring Boot)
- **SpringDoc OpenAPI** for API documentation
- **Micrometer + Prometheus** for metrics
- **JaCoCo** for code coverage

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
| GET | `/api/v1/topics/{name}/partitions/{partition}/producers` | Get partition producer state |
| GET | `/api/v1/topics/{name}/replicas/log-dirs` | Get replica log directories |

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
| GET | `/api/v1/cluster/brokers/{id}/log-dirs` | Get broker log directories |
| GET | `/api/v1/cluster/reassignments` | List partition reassignments |
| GET | `/api/v1/cluster/features` | List Kafka features |
| GET | `/api/v1/cluster/quorum` | Get metadata quorum info (KRaft) |

### Share Groups (`/api/v1/share-groups`)

| Method | Path | Description |
|--------|------|-------------|
| GET | `/api/v1/share-groups` | List all share groups |
| GET | `/api/v1/share-groups/{groupId}` | Get share group details |

### ACLs (`/api/v1/acls`)

| Method | Path | Description |
|--------|------|-------------|
| GET | `/api/v1/acls` | List all ACLs |

### Transactions (`/api/v1/transactions`)

| Method | Path | Description |
|--------|------|-------------|
| GET | `/api/v1/transactions` | List all transactions |
| GET | `/api/v1/transactions/{transactionalId}` | Get transaction details |

### Quotas (`/api/v1/quotas`)

| Method | Path | Description |
|--------|------|-------------|
| GET | `/api/v1/quotas` | List client quotas |

### Delegation Tokens (`/api/v1/tokens`)

| Method | Path | Description |
|--------|------|-------------|
| GET | `/api/v1/tokens` | List delegation tokens |

### Users (`/api/v1/users`)

| Method | Path | Description |
|--------|------|-------------|
| GET | `/api/v1/users/credentials` | List user SCRAM credentials |

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
│   ├── ProducerState.java             # Producer state model
│   ├── ReplicaLogDirInfo.java         # Replica log dir model
│   ├── TopicResponse.java             # List response DTO
│   ├── TopicDetailResponse.java       # Detail response DTO
│   ├── TopicPartitionInfoResponse.java # Partition DTO
│   ├── ProducerStateResponse.java     # Producer state DTO
│   ├── ReplicaLogDirInfoResponse.java # Replica log dir DTO
│   ├── TopicService.java              # Business logic
│   ├── TopicController.java           # REST controller
│   └── TopicNotFoundException.java    # Exception
├── consumergroup/                       # Consumer group feature
│   ├── ConsumerGroup.java              # Domain model
│   ├── ConsumerGroupMember.java        # Member model
│   ├── ConsumerGroupOffset.java        # Offset model
│   ├── ConsumerGroupResponse.java      # List response DTO
│   ├── ConsumerGroupDetailResponse.java # Detail response DTO
│   ├── ConsumerGroupMemberResponse.java # Member DTO
│   ├── ConsumerGroupOffsetResponse.java # Offset DTO
│   ├── ConsumerGroupService.java       # Business logic
│   ├── ConsumerGroupController.java    # REST controller
│   └── ConsumerGroupNotFoundException.java
├── cluster/                             # Cluster feature
│   ├── Broker.java                     # Broker model
│   ├── ClusterInfo.java                # Cluster info model
│   ├── LogDirInfo.java                # Log directory model
│   ├── LogDirPartition.java           # Log dir partition model
│   ├── PartitionReassignment.java     # Reassignment model
│   ├── KafkaFeature.java             # Feature model
│   ├── QuorumInfo.java               # Quorum info model
│   ├── QuorumReplica.java            # Quorum replica model
│   ├── BrokerResponse.java            # Broker list DTO
│   ├── BrokerDetailResponse.java      # Broker detail DTO
│   ├── ClusterInfoResponse.java       # Cluster info DTO
│   ├── LogDirInfoResponse.java        # Log dir DTO
│   ├── LogDirPartitionResponse.java   # Log dir partition DTO
│   ├── PartitionReassignmentResponse.java # Reassignment DTO
│   ├── KafkaFeatureResponse.java      # Feature DTO
│   ├── QuorumInfoResponse.java        # Quorum info DTO
│   ├── QuorumReplicaResponse.java     # Quorum replica DTO
│   ├── ClusterService.java            # Business logic
│   ├── ClusterController.java         # REST controller
│   └── BrokerNotFoundException.java   # Exception
├── sharegroup/                           # Share group feature
│   ├── ShareGroup.java                 # Domain model
│   ├── ShareGroupMember.java           # Member model
│   ├── ShareGroupResponse.java         # List response DTO
│   ├── ShareGroupDetailResponse.java   # Detail response DTO
│   ├── ShareGroupMemberResponse.java   # Member DTO
│   ├── ShareGroupService.java          # Business logic
│   ├── ShareGroupController.java       # REST controller
│   └── ShareGroupNotFoundException.java # Exception
├── acl/                                 # ACL feature
│   ├── Acl.java                       # Domain model
│   ├── AclResponse.java              # Response DTO
│   ├── AclService.java               # Business logic
│   └── AclController.java            # REST controller
├── transaction/                         # Transaction feature
│   ├── TransactionListing.java        # Transaction listing model
│   ├── TransactionDetail.java         # Transaction detail model
│   ├── TransactionTopicPartition.java # Topic partition model
│   ├── TransactionListingResponse.java # Listing DTO
│   ├── TransactionDetailResponse.java # Detail DTO
│   ├── TransactionTopicPartitionResponse.java # Topic partition DTO
│   ├── TransactionService.java        # Business logic
│   ├── TransactionController.java     # REST controller
│   └── TransactionNotFoundException.java # Exception
├── quota/                               # Quota feature
│   ├── ClientQuota.java               # Domain model
│   ├── ClientQuotaResponse.java       # Response DTO
│   ├── QuotaService.java             # Business logic
│   └── QuotaController.java          # REST controller
├── token/                               # Delegation token feature
│   ├── DelegationToken.java           # Domain model
│   ├── DelegationTokenResponse.java   # Response DTO
│   ├── TokenService.java             # Business logic
│   └── TokenController.java          # REST controller
└── user/                                # User SCRAM credential feature
    ├── UserScramCredential.java       # Domain model
    ├── ScramCredentialInfo.java       # Credential info model
    ├── UserScramCredentialResponse.java # Response DTO
    ├── ScramCredentialInfoResponse.java # Credential info DTO
    ├── UserService.java               # Business logic
    └── UserController.java           # REST controller
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

# Generate code coverage report
./gradlew jacocoTestReport
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

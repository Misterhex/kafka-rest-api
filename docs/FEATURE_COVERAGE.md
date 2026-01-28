# Kafka AdminClient Feature Coverage

This document tracks feature parity between Kafka's AdminClient API and this REST API.

**Last Updated:** 2026-01-29
**Kafka Version Reference:** 4.x

---

## Coverage Summary

| Category | Read Ops Implemented | Read Ops Missing | Write Ops (Out of Scope) |
|----------|---------------------|------------------|--------------------------|
| Topics | 3/3 | 0 | 3 |
| Consumer Groups | 3/3 | 0 | 4 |
| Share Groups (4.0+) | 1/1 | 0 | 0 |
| Cluster/Brokers | 4/4 | 0 | 1 |
| KRaft (4.0+) | 1/1 | 0 | 2 |
| ACLs | 1/1 | 0 | 2 |
| Quotas | 1/1 | 0 | 1 |
| Partitions/Replication | 1/1 | 0 | 2 |
| Transactions | 3/3 | 0 | 2 |
| Delegation Tokens | 1/1 | 0 | 3 |
| User Credentials | 1/1 | 0 | 1 |
| Features/Metadata | 1/1 | 0 | 1 |
| **Total** | **21/21 (100%)** | **0** | **22** |

---

## Detailed Feature Matrix

### Topics

| AdminClient Method | Implemented | API Endpoint | Notes |
|-------------------|-------------|--------------|-------|
| `listTopics` | :white_check_mark: | `GET /api/v1/topics` | |
| `describeTopics` | :white_check_mark: | `GET /api/v1/topics/{name}` | Includes partition info |
| `describeConfigs` (topic) | :white_check_mark: | `GET /api/v1/topics/{name}` | Non-default configs only |
| `createTopics` | :x: | - | Write operation |
| `deleteTopics` | :x: | - | Write operation |
| `createPartitions` | :x: | - | Write operation |

### Consumer Groups

| AdminClient Method | Implemented | API Endpoint | Notes |
|-------------------|-------------|--------------|-------|
| `listConsumerGroups` | :white_check_mark: | `GET /api/v1/consumer-groups` | |
| `describeConsumerGroups` | :white_check_mark: | `GET /api/v1/consumer-groups/{groupId}` | Includes members |
| `listConsumerGroupOffsets` | :white_check_mark: | `GET /api/v1/consumer-groups/{groupId}/offsets` | With lag calculation |
| `deleteConsumerGroups` | :x: | - | Write operation |
| `alterConsumerGroupOffsets` | :x: | - | Write operation |
| `deleteConsumerGroupOffsets` | :x: | - | Write operation |
| `removeMembersFromConsumerGroup` | :x: | - | Write operation |

### Share Groups (Kafka 4.0+)

| AdminClient Method | Implemented | API Endpoint | Notes |
|-------------------|-------------|--------------|-------|
| `describeShareGroups` | :white_check_mark: | `GET /api/v1/share-groups/{groupId}` | Share group details with members |

### Cluster & Brokers

| AdminClient Method | Implemented | API Endpoint | Notes |
|-------------------|-------------|--------------|-------|
| `describeCluster` | :white_check_mark: | `GET /api/v1/cluster` | Cluster ID, controller, brokers |
| `describeConfigs` (broker) | :white_check_mark: | `GET /api/v1/cluster/brokers/{id}` | Non-default, non-sensitive |
| `describeLogDirs` | :white_check_mark: | `GET /api/v1/cluster/brokers/{id}/log-dirs` | Disk usage per broker |
| `describeReplicaLogDirs` | :white_check_mark: | `GET /api/v1/topics/{name}/replicas/log-dirs` | Replica log directory info |
| `unregisterBroker` | :x: | - | Write operation |

### KRaft Management (Kafka 4.0+)

| AdminClient Method | Implemented | API Endpoint | Notes |
|-------------------|-------------|--------------|-------|
| `describeMetadataQuorum` | :white_check_mark: | `GET /api/v1/cluster/quorum` | KRaft quorum status |
| `addRaftVoter` | :x: | - | Write operation |
| `removeRaftVoter` | :x: | - | Write operation |

### Offsets & Records

| AdminClient Method | Implemented | API Endpoint | Notes |
|-------------------|-------------|--------------|-------|
| `listOffsets` | :white_check_mark: | (internal) | Used for lag calculation |
| `deleteRecords` | :x: | - | Write operation |

### ACLs

| AdminClient Method | Implemented | API Endpoint | Notes |
|-------------------|-------------|--------------|-------|
| `describeAcls` | :white_check_mark: | `GET /api/v1/acls` | Security/authorization visibility |
| `createAcls` | :x: | - | Write operation |
| `deleteAcls` | :x: | - | Write operation |

### Quotas

| AdminClient Method | Implemented | API Endpoint | Notes |
|-------------------|-------------|--------------|-------|
| `describeClientQuotas` | :white_check_mark: | `GET /api/v1/quotas` | Rate limiting visibility |
| `alterClientQuotas` | :x: | - | Write operation |

### Partitions & Replication

| AdminClient Method | Implemented | API Endpoint | Notes |
|-------------------|-------------|--------------|-------|
| `listPartitionReassignments` | :white_check_mark: | `GET /api/v1/cluster/reassignments` | In-progress reassignments |
| `alterPartitionReassignments` | :x: | - | Write operation |
| `electLeaders` | :x: | - | Write operation |

### Transactions

| AdminClient Method | Implemented | API Endpoint | Notes |
|-------------------|-------------|--------------|-------|
| `listTransactions` | :white_check_mark: | `GET /api/v1/transactions` | List active transactions |
| `describeTransactions` | :white_check_mark: | `GET /api/v1/transactions/{transactionalId}` | Transaction details |
| `describeProducers` | :white_check_mark: | `GET /api/v1/topics/{name}/partitions/{partition}/producers` | Producer state per partition |
| `abortTransaction` | :x: | - | Write operation |
| `fenceProducers` | :x: | - | Write operation |

### Delegation Tokens

| AdminClient Method | Implemented | API Endpoint | Notes |
|-------------------|-------------|--------------|-------|
| `describeDelegationToken` | :white_check_mark: | `GET /api/v1/tokens` | Token management visibility |
| `createDelegationToken` | :x: | - | Write operation |
| `renewDelegationToken` | :x: | - | Write operation |
| `expireDelegationToken` | :x: | - | Write operation |

### User SCRAM Credentials

| AdminClient Method | Implemented | API Endpoint | Notes |
|-------------------|-------------|--------------|-------|
| `describeUserScramCredentials` | :white_check_mark: | `GET /api/v1/users/credentials` | User credential info |
| `alterUserScramCredentials` | :x: | - | Write operation |

### Features & Metadata

| AdminClient Method | Implemented | API Endpoint | Notes |
|-------------------|-------------|--------------|-------|
| `describeFeatures` | :white_check_mark: | `GET /api/v1/cluster/features` | Cluster feature flags |
| `updateFeatures` | :x: | - | Write operation |

---

## Missing Read Operations (Backlog)

All read operations are implemented. No missing read operations.

---

## References

- [Kafka Admin API Javadoc (4.0)](https://kafka.apache.org/40/javadoc/org/apache/kafka/clients/admin/Admin.html)
- [Kafka Documentation](https://kafka.apache.org/documentation/)

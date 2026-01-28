# Kafka AdminClient Feature Coverage

This document tracks feature parity between Kafka's AdminClient API and this REST API.

**Last Updated:** 2026-01-28
**Kafka Version Reference:** 4.x

---

## Coverage Summary

| Category | Read Ops Implemented | Read Ops Missing | Write Ops (Out of Scope) |
|----------|---------------------|------------------|--------------------------|
| Topics | 3/3 | 0 | 3 |
| Consumer Groups | 3/3 | 0 | 4 |
| Share Groups (4.0+) | 0/1 | 1 | 0 |
| Cluster/Brokers | 2/4 | 2 | 1 |
| KRaft Management (4.0+) | 0/1 | 1 | 2 |
| ACLs | 0/1 | 1 | 2 |
| Quotas | 0/1 | 1 | 1 |
| Partitions/Replication | 0/1 | 1 | 2 |
| Transactions | 0/3 | 3 | 2 |
| Delegation Tokens | 0/1 | 1 | 3 |
| User Credentials | 0/1 | 1 | 1 |
| Features/Metadata | 0/2 | 2 | 1 |
| **Total** | **8/22 (36%)** | **14** | **22** |

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
| `describeShareGroups` | :x: | - | **TODO**: New in Kafka 4.0 |

### Cluster & Brokers

| AdminClient Method | Implemented | API Endpoint | Notes |
|-------------------|-------------|--------------|-------|
| `describeCluster` | :white_check_mark: | `GET /api/v1/cluster` | Cluster ID, controller, brokers |
| `describeConfigs` (broker) | :white_check_mark: | `GET /api/v1/cluster/brokers/{id}` | Non-default, non-sensitive |
| `describeLogDirs` | :x: | - | **TODO**: Disk usage per broker/topic |
| `describeReplicaLogDirs` | :x: | - | **TODO**: Replica log directory info |
| `unregisterBroker` | :x: | - | Write operation |

### KRaft Management (Kafka 4.0+)

| AdminClient Method | Implemented | API Endpoint | Notes |
|-------------------|-------------|--------------|-------|
| `describeMetadataQuorum` | :x: | - | **TODO**: KRaft quorum status |
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
| `describeAcls` | :x: | - | **TODO**: Security/authorization visibility |
| `createAcls` | :x: | - | Write operation |
| `deleteAcls` | :x: | - | Write operation |

### Quotas

| AdminClient Method | Implemented | API Endpoint | Notes |
|-------------------|-------------|--------------|-------|
| `describeClientQuotas` | :x: | - | **TODO**: Rate limiting visibility |
| `alterClientQuotas` | :x: | - | Write operation |

### Partitions & Replication

| AdminClient Method | Implemented | API Endpoint | Notes |
|-------------------|-------------|--------------|-------|
| `listPartitionReassignments` | :x: | - | **TODO**: In-progress reassignments |
| `alterPartitionReassignments` | :x: | - | Write operation |
| `electLeaders` | :x: | - | Write operation |

### Transactions

| AdminClient Method | Implemented | API Endpoint | Notes |
|-------------------|-------------|--------------|-------|
| `listTransactions` | :x: | - | **TODO**: List active transactions |
| `describeTransactions` | :x: | - | **TODO**: Transaction details |
| `describeProducers` | :x: | - | **TODO**: Producer state per partition |
| `abortTransaction` | :x: | - | Write operation |
| `fenceProducers` | :x: | - | Write operation |

### Delegation Tokens

| AdminClient Method | Implemented | API Endpoint | Notes |
|-------------------|-------------|--------------|-------|
| `describeDelegationToken` | :x: | - | **TODO**: Token management visibility |
| `createDelegationToken` | :x: | - | Write operation |
| `renewDelegationToken` | :x: | - | Write operation |
| `expireDelegationToken` | :x: | - | Write operation |

### User SCRAM Credentials

| AdminClient Method | Implemented | API Endpoint | Notes |
|-------------------|-------------|--------------|-------|
| `describeUserScramCredentials` | :x: | - | **TODO**: User credential info |
| `alterUserScramCredentials` | :x: | - | Write operation |

### Features & Metadata

| AdminClient Method | Implemented | API Endpoint | Notes |
|-------------------|-------------|--------------|-------|
| `describeFeatures` | :x: | - | **TODO**: Cluster feature flags |
| `updateFeatures` | :x: | - | Write operation |

---

## Missing Read Operations (Backlog)

Prioritized list of read operations to implement:

### High Priority
| Feature | AdminClient Method | Suggested Endpoint | Use Case |
|---------|-------------------|-------------------|----------|
| Log Dirs | `describeLogDirs` | `GET /api/v1/cluster/brokers/{id}/log-dirs` | Disk usage monitoring |
| Partition Reassignments | `listPartitionReassignments` | `GET /api/v1/partitions/reassignments` | Rebalance monitoring |

### Medium Priority
| Feature | AdminClient Method | Suggested Endpoint | Use Case |
|---------|-------------------|-------------------|----------|
| ACLs | `describeAcls` | `GET /api/v1/acls` | Security audit |
| Transactions | `listTransactions` | `GET /api/v1/transactions` | Transaction monitoring |
| Transaction Details | `describeTransactions` | `GET /api/v1/transactions/{id}` | Debugging stuck transactions |
| Client Quotas | `describeClientQuotas` | `GET /api/v1/quotas` | Rate limit visibility |

### Low Priority
| Feature | AdminClient Method | Suggested Endpoint | Use Case |
|---------|-------------------|-------------------|----------|
| Features | `describeFeatures` | `GET /api/v1/cluster/features` | Version compatibility |
| Metadata Quorum | `describeMetadataQuorum` | `GET /api/v1/cluster/quorum` | KRaft health |
| Share Groups | `describeShareGroups` | `GET /api/v1/share-groups` | Kafka 4.0 share groups |
| Producers | `describeProducers` | `GET /api/v1/topics/{name}/producers` | Producer debugging |
| Delegation Tokens | `describeDelegationToken` | `GET /api/v1/tokens` | Token audit |
| SCRAM Credentials | `describeUserScramCredentials` | `GET /api/v1/users/{user}/credentials` | User management |
| Replica Log Dirs | `describeReplicaLogDirs` | `GET /api/v1/topics/{name}/replicas/log-dirs` | Replica placement |

---

## References

- [Kafka Admin API Javadoc (4.0)](https://kafka.apache.org/40/javadoc/org/apache/kafka/clients/admin/Admin.html)
- [Kafka Documentation](https://kafka.apache.org/documentation/)

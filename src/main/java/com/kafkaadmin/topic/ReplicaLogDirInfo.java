package com.kafkaadmin.topic;

/**
 * Domain model representing replica log directory information.
 *
 * @param brokerId broker ID
 * @param partition partition number
 * @param currentLogDir current log directory path
 * @param futureLogDir future log directory path (null if not moving)
 * @param currentReplicaSize current replica size in bytes
 * @param currentReplicaOffsetLag current replica offset lag
 * @param futureReplicaSize future replica size in bytes (-1 if not moving)
 * @param futureReplicaOffsetLag future replica offset lag (-1 if not moving)
 */
public record ReplicaLogDirInfo(
        int brokerId,
        int partition,
        String currentLogDir,
        String futureLogDir,
        long currentReplicaSize,
        long currentReplicaOffsetLag,
        long futureReplicaSize,
        long futureReplicaOffsetLag
) {
}

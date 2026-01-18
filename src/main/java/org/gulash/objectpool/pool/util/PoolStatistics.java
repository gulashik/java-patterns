package org.gulash.objectpool.pool.util;

/**
 * Статистика использования пула объектов.
 */
public class PoolStatistics {
    private final int totalObjects;
    private final int availableObjects;
    private final int inUseObjects;
    private final long totalAcquires;
    private final long totalReleases;
    private final long timeoutCount;
    private final long averageWaitTimeMs;

    public PoolStatistics(int totalObjects, int availableObjects, int inUseObjects,
                          long totalAcquires, long totalReleases, long timeoutCount,
                          long averageWaitTimeMs) {
        this.totalObjects = totalObjects;
        this.availableObjects = availableObjects;
        this.inUseObjects = inUseObjects;
        this.totalAcquires = totalAcquires;
        this.totalReleases = totalReleases;
        this.timeoutCount = timeoutCount;
        this.averageWaitTimeMs = averageWaitTimeMs;
    }

    // Getters
    public int getTotalObjects() { return totalObjects; }
    public int getAvailableObjects() { return availableObjects; }
    public int getInUseObjects() { return inUseObjects; }
    public long getTotalAcquires() { return totalAcquires; }
    public long getTotalReleases() { return totalReleases; }
    public long getTimeoutCount() { return timeoutCount; }
    public long getAverageWaitTimeMs() { return averageWaitTimeMs; }

    @Override
    public String toString() {
        return String.format(
            "PoolStatistics{total=%d, available=%d, inUse=%d, acquires=%d, releases=%d, timeouts=%d, avgWaitMs=%d}",
            totalObjects, availableObjects, inUseObjects, totalAcquires, totalReleases, timeoutCount, averageWaitTimeMs
        );
    }
}

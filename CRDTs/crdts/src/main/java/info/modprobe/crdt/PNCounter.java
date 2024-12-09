package info.modprobe.crdt;

/**
 * A Positive-Negative Counter (PN-Counter) CRDT that supports both increments and decrements.
 * It uses two G-Counters internally: one for increments and one for decrements.
 *
 * # Properties
 * - **Convergent**: All replicas eventually reach the same state
 * - **Commutative**: Order of operations doesn't matter
 *
 * # Implementation
 * Uses two G-Counters:
 * - `increments`: tracks all increment operations
 * - `decrements`: tracks all decrement operations
 * Final value is calculated as `increments - decrements`
 */
public interface PNCounter {

    /**
     * Increments the counter for the specified replica
     */
    void increment(String replicaId);

    /**
     * Decrements the counter for the specified replica
     */
    void decrement(String replicaId);

    /**
     * Gets the current value (increments - decrements)
     */
    long getValue();

    /**
     * Merges another PN-Counter into this one
     */
    default void mergeExternal(PNCounter other) {
    }

    void merge();

}

package info.modprobe.crdt;

import java.util.Map;

/**
 *  Grow only counter allows only increments.
 *  You can think of this as a distributed `AtomicLong` that only allows increments,
 *  the end value is obtained with `getValue`
 *
 *  Has the following properties:
 *
 * # Key Properties
 * - **Monotonic**: Values can only increase
 * - **Convergent**: All replicas eventually reach the same state
 * - **Commutative**: Order of operations doesn't matter
*/
public interface GCounter {

    void increment(String replicaId);

    long getValue();

    void mergeExternal(GCounter other);

    void merge();

    Map<String, Long> getValues();
}

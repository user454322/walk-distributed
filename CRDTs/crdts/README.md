# Simple library to understrand how CRDTs counters work

*  Grow only counter `GCounter`. [Source](https://github.com/user454322/walk-distributed/blob/main/CRDTs/crdts/src/main/java/info/modprobe/crdt/GCounter.java).
```java
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

}
```

   
* Positive-negative counter allows increments and decrements. [Source](https://github.com/user454322/walk-distributed/blob/main/CRDTs/crdts/src/main/java/info/modprobe/crdt/PNCounter.java)
```java
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

    void increment(String replicaId);

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
}
```

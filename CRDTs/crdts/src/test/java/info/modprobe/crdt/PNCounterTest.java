package info.modprobe.crdt;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class PNCounterTest {
    @Test
    void testBasicOperations() {
        PNCounter counter = new BasicPNCounter();

        counter.increment("replica1");
        assertEquals(1, counter.getValue());

        counter.decrement("replica1");
        assertEquals(0, counter.getValue());

        counter.decrement("replica1");
        assertEquals(-1, counter.getValue());
    }

    @Test
    void testMultipleReplicas() {
        PNCounter counter = new BasicPNCounter();

        // Replica 1 increments twice
        counter.increment("replica1");
        counter.increment("replica1");

        // Replica 2 decrements once
        counter.decrement("replica2");

        assertEquals(1, counter.getValue());
    }

    @Test
    void testMerge() {
        PNCounter counter1 = new BasicPNCounter();
        PNCounter counter2 = new BasicPNCounter();

        // Counter 1: +2 from replica1
        counter1.increment("replica1");
        counter1.increment("replica1");

        // Counter 2: -1 from replica2
        counter2.decrement("replica2");

        // Initial state assertions
        assertEquals(2, counter1.getValue());
        assertEquals(-1, counter2.getValue());

        // Merge counter2 into counter1
        counter1.mergeExternal(counter2);

        // After merge: +2 (from replica1) - 1 (from replica2) = 1
        assertEquals(1, counter1.getValue());
    }

    @Test
    void testConcurrentUpdates() {
        PNCounter counter1 = new BasicPNCounter();
        PNCounter counter2 = new BasicPNCounter();

        // Simulate concurrent updates
        counter1.increment("replica1"); // +1
        counter2.decrement("replica1"); // -1
        counter1.increment("replica2"); // +1
        counter2.decrement("replica2"); // -1

        // Create two merged counters in different orders
        PNCounter merged1 = new BasicPNCounter();
        PNCounter merged2 = new BasicPNCounter();

        merged1.mergeExternal(counter1);
        merged1.mergeExternal(counter2);

        merged2.mergeExternal(counter2);
        merged2.mergeExternal(counter1);

        // Verify convergence
        assertEquals(merged1.getValue(), merged2.getValue());
    }

    @Test
    void testIdempotency() {
        PNCounter counter1 = new BasicPNCounter();
        PNCounter counter2 = new BasicPNCounter();

        counter1.increment("replica1");
        counter2.decrement("replica2");

        // Merge multiple times
        counter1.mergeExternal(counter2);
        long firstMergeValue = counter1.getValue();

        counter1.mergeExternal(counter2);
        long secondMergeValue = counter1.getValue();

        // Values should be the same after multiple merges
        assertEquals(firstMergeValue, secondMergeValue);
    }
}

package info.modprobe.crdt;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.util.Map;


public class GCounterTest {

    @Test
    void testBasicIncrement() {
        GCounter counter = new BasicGCounter();
        counter.increment("replica1");
        assertEquals(1, counter.getValue());
        counter.increment("replica1");
        assertEquals(2, counter.getValue());
    }

    @Test
    void testMultipleReplicas() {
        GCounter counter = new BasicGCounter();
        counter.increment("replica1");
        counter.increment("replica2");
        counter.increment("replica1");

        assertEquals(3, counter.getValue());
        Map<String, Long> values = counter.getValues();
        assertEquals(2, values.get("replica1"));
        assertEquals(1, values.get("replica2"));
    }

    @Test
    void testMerge() {
        GCounter counter1 = new BasicGCounter();
        GCounter counter2 = new BasicGCounter();

        // Simulate updates in different replicas
        counter1.increment("replica1");
        counter1.increment("replica1");  // replica1 = 2
        counter2.increment("replica2");
        counter2.increment("replica2");
        counter2.increment("replica2");  // replica2 = 3

        // Initial state assertions
        assertEquals(2, counter1.getValue());
        assertEquals(3, counter2.getValue());

        // Merge counter2 into counter1
        counter1.mergeExternal(counter2);

        // After merge assertions
        assertEquals(5, counter1.getValue());
        Map<String, Long> mergedValues = counter1.getValues();
        assertEquals(2, mergedValues.get("replica1"));
        assertEquals(3, mergedValues.get("replica2"));
    }

    @Test
    void testConcurrentUpdates() {
        GCounter counter1 = new BasicGCounter();
        GCounter counter2 = new BasicGCounter();

        // Simulate concurrent updates
        counter1.increment("replica1");
        counter2.increment("replica1");
        counter1.increment("replica2");
        counter2.increment("replica2");

        // Merge both ways
        GCounter merged1 = new BasicGCounter();
        GCounter merged2 = new BasicGCounter();

        merged1.mergeExternal(counter1);
        merged1.mergeExternal(counter2);

        merged2.mergeExternal(counter2);
        merged2.mergeExternal(counter1);

        // Verify convergence
        assertEquals(merged1.getValue(), merged2.getValue());
        assertEquals(merged1.getValues(), merged2.getValues());
    }

    @Test
    void testIdempotency() {
        GCounter counter1 = new BasicGCounter();
        GCounter counter2 = new BasicGCounter();

        counter1.increment("replica1");
        counter2.increment("replica2");

        // Merge multiple times
        counter1.mergeExternal(counter2);
        long firstMergeValue = counter1.getValue();
        counter1.mergeExternal(counter2);
        long secondMergeValue = counter1.getValue();

        // Values should be the same after multiple merges
        assertEquals(firstMergeValue, secondMergeValue);
    }
}


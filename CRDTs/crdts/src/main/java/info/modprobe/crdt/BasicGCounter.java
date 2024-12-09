package info.modprobe.crdt;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class BasicGCounter implements GCounter, Serializable {

    private static final long serialVersionUID = 1L;


    final Map<String, Long> values;

    public BasicGCounter () {
        values = new HashMap<>();
    }

    @Override
    public void increment(final String replicaId) {
        values.merge(replicaId, 1L, Long::sum);
    }

    @Override
    public long getValue() {
        return values.values().stream()
                .mapToLong(Long::longValue)
                .sum();
    }

    @Override
    public void mergeExternal(final GCounter other) {
        if (other != null) {
            other.getValues().forEach((replicaId, count) ->
                    values.merge(replicaId, count, Math::max));

        }
    }

    @Override
    public void merge() {
        values.forEach((k,v) -> values.merge(k,v, Math::max));
    }

    @Override
    public Map<String, Long> getValues() {
        return Collections.unmodifiableMap(values);
    }
}

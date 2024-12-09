package info.modprobe.crdt;

import java.io.Serializable;

public class BasicPNCounter implements PNCounter, Serializable {

    private static final long serialVersionUID = 1L;


    private final GCounter increments;
    private final GCounter decrements;

    public BasicPNCounter() {
        this.increments = new BasicGCounter();
        this.decrements = new BasicGCounter();
    }

    @Override
    public void increment(final String replicaId) {
        increments.increment(replicaId);
    }

    @Override
    public void decrement(final String replicaId) {
        decrements.increment(replicaId);
    }

    @Override
    public long getValue() {
        return increments.getValue() - decrements.getValue();
    }

   @Override
    public void mergeExternal(final PNCounter other) {
        if (other instanceof BasicPNCounter) {
            final BasicPNCounter otherPNCounter =  (BasicPNCounter) other;
            this.increments.mergeExternal(otherPNCounter.increments);
            this.decrements.mergeExternal(otherPNCounter.decrements);

        } else {
            throw new UnsupportedOperationException("This class only supports merging BasicPNCounter objects");
        }
    }

    @Override
    public void merge() {
        this.increments.merge();
        this.decrements.merge();
    }
}


package info.modprobe.crdt.demo.us2024elections;

import info.modprobe.crdt.BasicGCounter;
import info.modprobe.crdt.GCounter;

import java.io.Serializable;

class Counter implements Serializable {

    private final GCounter kamala;
    private final GCounter trump;

    private final String location;

    Counter(final String location) {
        this.location = location;
        kamala = new BasicGCounter();
        trump = new BasicGCounter();
    }


    /**
     * Adds one vote for Kamala and returns the current number
     * of votes for Kamala
     *
     * @return
     */
    long voteForKamala() {
        kamala.increment(location);
        long value = kamala.getValue();
        return value;
    }

    /**
     * Adds one vote for Trump and returns the current number
     * of votes for Trump
     *
     * @return
     */
    long voteForTrump() {
        trump.increment(location);
        long value = trump.getValue();
        return value;
    }

    void merge(final Counter other) {
        kamala.mergeExternal(other.kamala);
        trump.mergeExternal(other.trump);
    }

    Results getResults() {
        return new Results(trump.getValue(), kamala.getValue());
    }

    public record Results(long kamala, long trump) {};
}

package org.mangorage.testbus;

public class Timed {
    public static Timed of(ITimed timed) {
        return new Timed(timed);
    }

    private final ITimed timedInstance;

    private Timed(ITimed timedInstance) {
        this.timedInstance = timedInstance;
    }

    public long getResult() {
        var before = System.nanoTime();
        timedInstance.init();
        return System.nanoTime() - before;
    }


    public interface ITimed {
        void init();
    }
}

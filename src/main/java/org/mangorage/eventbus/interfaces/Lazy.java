package org.mangorage.eventbus.interfaces;

import java.util.function.Supplier;

public interface Lazy<T> extends Supplier<T> {
    void invalidate();

    /**
     * Constructs a lazy-initialized object
     * @param supplier The supplier for the value, to be called the first time the value is needed.
     */
    static <T> Lazy<T> of(Supplier<T> supplier) {
        return new Lazy.Fast<T>(supplier);
    }

    /**
     * Constructs a thread-safe lazy-initialized object
     * @param supplier The supplier for the value, to be called the first time the value is needed.
     */
    static <T> Lazy<T> concurrentOf(Supplier<T> supplier) {
        return new Lazy.Concurrent<T>(supplier);
    }

    /**
     * Non-thread-safe implementation.
     */
    final class Fast<T> implements Lazy<T> {
        private final Supplier<T> supplier;
        private T instance;

        private Fast(Supplier<T> supplier) {
            this.supplier = supplier;
        }

        @Override
        public final T get() {
            if (instance == null) {
                instance = supplier.get();
            }
            return instance;
        }

        @Override
        public void invalidate() {
            this.instance = null;
        }
    }

    /**
     * Thread-safe implementation.
     */
    final class Concurrent<T> implements Lazy<T> {
        private final Object lock = new Object();
        private final Supplier<T> supplier;
        private volatile T instance;

        private Concurrent(Supplier<T> supplier) {
            this.supplier = supplier;
        }

        @Override
        public final T get()
        {
            var ret = instance;
            if (ret == null) {
                synchronized (lock) {
                    if (instance == null) {
                        return instance = supplier.get();
                    }
                }
            }
            return ret;
        }

        @Override
        public void invalidate() {
            synchronized (lock) {
                this.instance = null;
            }
        }
    }
}

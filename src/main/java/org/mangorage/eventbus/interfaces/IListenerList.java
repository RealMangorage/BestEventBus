package org.mangorage.eventbus.interfaces;

import java.util.function.BiConsumer;

public interface IListenerList<S extends IEventState> {
    void register(BiConsumer<?, S> consumer);
    void post(Object object);
}

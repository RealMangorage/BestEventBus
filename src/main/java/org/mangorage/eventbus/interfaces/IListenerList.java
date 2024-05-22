package org.mangorage.eventbus.interfaces;

import org.mangorage.eventbus.EventKey;

import java.util.List;
import java.util.function.BiConsumer;

public interface IListenerList<S extends IEventState> {
    void register(BiConsumer<?, S> consumer, EventKey<?, S> eventKey);
    void post(Object object);
    List<BiConsumer<Object, S>> getListeners();
}

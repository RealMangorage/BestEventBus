package org.mangorage.eventbus.interfaces;

import org.mangorage.eventbus.EventKey;

import java.util.List;
import java.util.function.BiConsumer;

public interface IListenerList<S extends IEventState> {
    void register(BiConsumer<?, S> consumer, EventKey<?, S> eventKey);
    void post(Object object);

    void addChild(IListenerList<S> child);
    void cacheListenerList();

    List<BiConsumer<Object, S>> getListeners();
    List<BiConsumer<Object, S>> getAllListeners();
}

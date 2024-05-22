package org.mangorage.eventbus.interfaces;

import org.mangorage.eventbus.EventKey;

import java.util.function.BiConsumer;

public interface IEventBus {
    <E, S extends IEventState> void addListener(EventKey<E, S> eventKey, BiConsumer<E, S> consumer);
    void post(Object object, EventKey<?, ?> eventKey);
}

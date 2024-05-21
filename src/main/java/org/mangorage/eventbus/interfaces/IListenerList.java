package org.mangorage.eventbus.interfaces;

import org.mangorage.eventbus.EventKey;
import org.mangorage.eventbus.impl.AlternateEventState;

import java.util.function.BiConsumer;

public interface IListenerList<S extends IEventState> {
    void register(BiConsumer<?, S> consumer, EventKey<?, S> eventKey);
    void post(Object object);
}

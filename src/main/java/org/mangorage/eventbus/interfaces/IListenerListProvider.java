package org.mangorage.eventbus.interfaces;

import org.mangorage.eventbus.EventKey;

public interface IListenerListProvider<S extends IEventState> {
    IListenerList<S> create(EventKey<?, S> eventKey, IListenerList<S> parent);
}

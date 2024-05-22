package org.mangorage.eventbus.impl;

import org.mangorage.eventbus.EventKey;
import org.mangorage.eventbus.interfaces.IListenerList;

import java.util.function.BiConsumer;

public class AlternateEventListenerList extends AbstractListenerList<AlternateEventState> {
    public AlternateEventListenerList(EventKey<?, AlternateEventState> key, IListenerList<AlternateEventState> parent) {
        super(key, parent);
    }

    @Override
    public void post(Object object) {
        var state = new AlternateEventState();
        for (BiConsumer<Object, AlternateEventState> listener : getListeners()) {
            if (state.isCancelled()) break;
            listener.accept(object, state);
        }
    }
}

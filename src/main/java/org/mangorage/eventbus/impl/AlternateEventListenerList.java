package org.mangorage.eventbus.impl;

import org.mangorage.eventbus.EventKey;
import org.mangorage.eventbus.interfaces.IListenerList;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class AlternateEventListenerList implements IListenerList<AlternateEventState> {
    private final List<BiConsumer<Object, AlternateEventState>> listeners = new ArrayList<>();
    private final EventKey<?, AlternateEventState> EVENT_KEY;

    public AlternateEventListenerList(EventKey<?, AlternateEventState> key, IListenerList<AlternateEventState> parent) {
        this.EVENT_KEY = key;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void register(BiConsumer<? extends Object, AlternateEventState> consumer, EventKey<?, AlternateEventState> eventKey) {
        if (EVENT_KEY == eventKey) {
            listeners.add(((BiConsumer<Object, AlternateEventState>) consumer));
        } else {
            throw new IllegalStateException("Cannot register EventListener, mismatched EventKeys were supplied");
        }
    }

    @Override
    public void post(Object object) {
        var state = new AlternateEventState();
        for (BiConsumer<Object, AlternateEventState> listener : listeners) {
            listener.accept(object, state);
        }
    }
}

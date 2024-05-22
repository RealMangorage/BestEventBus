package org.mangorage.eventbus.impl;

import org.mangorage.eventbus.EventKey;
import org.mangorage.eventbus.interfaces.IEventState;
import org.mangorage.eventbus.interfaces.IListenerList;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public abstract class AbstractListenerList<S extends IEventState> implements IListenerList<S> {
    private final List<BiConsumer<Object, S>> listeners = new ArrayList<>();
    private final EventKey<?, S> eventKey;
    private final IListenerList<S> parent;

    public AbstractListenerList(EventKey<?, S> key, IListenerList<S> parent) {
        this.eventKey = key;
        this.parent = parent;
    }

    public List<BiConsumer<Object, S>> getListeners() {
        if (parent != null)
            return Stream.of(listeners, parent.getListeners())
                    .flatMap(List::stream)
                    .toList();
        return listeners;
    }

    protected IListenerList<S> getParent() {
        return parent;
    }

    public EventKey<?, S> getEventKey() {
        return eventKey;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void register(BiConsumer<? extends Object, S> consumer, EventKey<?, S> eventKey) {
        if (getEventKey() == eventKey) {
            listeners.add(((BiConsumer<Object, S>) consumer));
        } else {
            throw new IllegalStateException("Cannot register EventListener, mismatched EventKeys were supplied");
        }
    }

}

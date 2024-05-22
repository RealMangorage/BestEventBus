package org.mangorage.eventbus.impl;

import org.mangorage.eventbus.EventKey;
import org.mangorage.eventbus.interfaces.IEventState;
import org.mangorage.eventbus.interfaces.IListenerList;
import org.mangorage.eventbus.interfaces.Lazy;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public abstract class AbstractListenerList<S extends IEventState> implements IListenerList<S> {
    private final List<BiConsumer<Object, S>> listeners = new ArrayList<>();
    private final EventKey<?, S> eventKey;
    private final IListenerList<S> parent;
    private final Set<IListenerList<S>> children = new HashSet<>();
    private final Lazy<BiConsumer<Object, S>[]> allListeners;

    public AbstractListenerList(EventKey<?, S> key, IListenerList<S> parent) {
        this.eventKey = key;
        this.parent = parent;
        this.allListeners = Lazy.concurrentOf(() -> {
            if (parent != null) {
                return Stream.of(listeners, List.of(parent.getListeners()))
                        .flatMap(List::stream)
                        .toArray(BiConsumer[]::new);
            }
            return listeners.toArray(BiConsumer[]::new);
        });
        if (parent != null) parent.addChild(this);
    }

    @Override
    public void invalidate() {
        allListeners.invalidate();
    }

    @Override
    public void addChild(IListenerList<S> child) {
        children.add(child);
    }

    public BiConsumer<Object, S>[] getListeners() {
        return allListeners.get();
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
            invalidate();
            children.forEach(IListenerList::invalidate);
            if (parent != null) parent.invalidate();
        } else {
            throw new IllegalStateException("Cannot register EventListener, mismatched EventKeys were supplied");
        }
    }

}

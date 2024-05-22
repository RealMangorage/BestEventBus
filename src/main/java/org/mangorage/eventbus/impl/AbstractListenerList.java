package org.mangorage.eventbus.impl;

import org.mangorage.eventbus.EventKey;
import org.mangorage.eventbus.interfaces.IEventState;
import org.mangorage.eventbus.interfaces.IListenerList;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public abstract class AbstractListenerList<S extends IEventState> implements IListenerList<S> {
    private final List<BiConsumer<Object, S>> listeners = new ArrayList<>();
    private final EventKey<?, S> eventKey;
    private final IListenerList<S> parent;
    private final Set<IListenerList<S>> children = new HashSet<>();

    private volatile List<BiConsumer<Object, S>> allListeners = new ArrayList<>();

    public AbstractListenerList(EventKey<?, S> key, IListenerList<S> parent) {
        this.eventKey = key;
        this.parent = parent;
        if (this.parent != null)
            this.parent.addChild(this);
    }

    public List<BiConsumer<Object, S>> getListeners() {
        return listeners;
    }

    @Override
    public List<BiConsumer<Object, S>> getAllListeners() {
        return allListeners;
    }

    public void addChild(IListenerList<S> child) {
        children.add(child);
    }

    protected IListenerList<S> getParent() {
        return parent;
    }

    public EventKey<?, S> getEventKey() {
        return eventKey;
    }

    @Override
    public void cacheListenerList() {
        System.out.println(eventKey.eClass());

        if (parent != null) {
            allListeners = Stream.of(getListeners(), parent.getListeners())
                    .flatMap(List::stream)
                    .toList();
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void register(BiConsumer<? extends Object, S> consumer, EventKey<?, S> eventKey) {
        if (getEventKey() == eventKey) {
            listeners.add(((BiConsumer<Object, S>) consumer));
            cacheListenerList();
            children.forEach(IListenerList::cacheListenerList);
            if (parent != null) parent.cacheListenerList();
        } else {
            throw new IllegalStateException("Cannot register EventListener, mismatched EventKeys were supplied");
        }
    }

}

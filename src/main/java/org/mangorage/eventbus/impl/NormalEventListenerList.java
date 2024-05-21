package org.mangorage.eventbus.impl;

import org.mangorage.eventbus.EventKey;
import org.mangorage.eventbus.interfaces.IListenerList;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class NormalEventListenerList implements IListenerList<NormalEventState> {
    private final List<BiConsumer<Object, NormalEventState>> LISTENERS = new ArrayList<>();
    private final EventKey<?, NormalEventState> EVENT_KEY;
    private final IListenerList<NormalEventState> parent;

    public NormalEventListenerList(EventKey<?, NormalEventState> key, IListenerList<NormalEventState> parent) {
        this.EVENT_KEY = key;
        this.parent = parent;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void register(BiConsumer<? extends Object, NormalEventState> consumer, EventKey<?, NormalEventState> eventKey) {
        if (EVENT_KEY == eventKey) {
            LISTENERS.add(((BiConsumer<Object, NormalEventState>) consumer));
        } else {
            throw new IllegalStateException("Cannot register EventListener, mismatched EventKeys were supplied");
        }
    }

    @Override
    public void post(Object object) {
        var state = new NormalEventState();
        for (BiConsumer<Object, NormalEventState> listener : LISTENERS) {
            listener.accept(object, state);
        }
        if (this.parent != null) parent.post(object);
    }
}

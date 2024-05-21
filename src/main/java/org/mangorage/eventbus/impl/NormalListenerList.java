package org.mangorage.eventbus.impl;

import org.mangorage.eventbus.interfaces.IListenerList;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public class NormalListenerList implements IListenerList<NormalEventState> {
    private final List<BiConsumer<Object, NormalEventState>> listeners = new ArrayList<>();

    public NormalListenerList() {}

    @Override
    public void register(BiConsumer<? extends Object, NormalEventState> consumer) {
        listeners.add(((BiConsumer<Object, NormalEventState>) consumer));
    }

    @Override
    public void post(Object object) {
        var state = new NormalEventState();
        for (BiConsumer<Object, NormalEventState> listener : listeners) {
            listener.accept(object, state);
        }
    }
}

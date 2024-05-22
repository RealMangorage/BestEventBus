package org.mangorage.eventbus.impl;

import org.mangorage.eventbus.EventKey;
import org.mangorage.eventbus.interfaces.IListenerList;


import java.util.function.BiConsumer;

public class NormalEventListenerList extends AbstractListenerList<NormalEventState> {

    public NormalEventListenerList(EventKey<?, NormalEventState> key, IListenerList<NormalEventState> parent) {
        super(key, parent);
    }

    @Override
    public void post(Object object) {
        var state = new NormalEventState();
        for (BiConsumer<Object, NormalEventState> listener : getListeners()) {
            listener.accept(object, state);
        }
    }
}

package org.mangorage.eventbus;

import org.mangorage.eventbus.interfaces.IEventBus;
import org.mangorage.eventbus.interfaces.IEventState;
import org.mangorage.eventbus.interfaces.IListenerList;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

public final class EventBus implements IEventBus {

    public static IEventBus create() {
        return new EventBus();
    }

    private final Map<EventKey<?, ?>, IListenerList<? extends IEventState>> listeners = new ConcurrentHashMap<>();

    private EventBus() {}

    @SuppressWarnings("all")
    public <E, S extends IEventState> void addListener(EventKey<E, S> eventKey, BiConsumer<E, S> consumer) {
        getListenerList(eventKey).register(consumer, eventKey);
    }

    public void post(Object object, EventKey<?, ?> eventKey) {
        IListenerList<?> list = getListenerList(eventKey);
        if (list != null) list.post(object);
    }

    @SuppressWarnings("unchecked")
    private  <S extends IEventState> IListenerList<S> getListenerList(EventKey<?, S> eventKey) {
        var list = listeners.get(eventKey);
        if (list != null) {
            return (IListenerList<S>) list;
        }

        if (eventKey.eClass() == Object.class) {
            return null;
        }

        return (IListenerList<S>) listeners.computeIfAbsent(eventKey, l -> eventKey.supplier().create(eventKey, getListenerList(eventKey.createNew(eventKey.eClass().getSuperclass()))));
    }
}

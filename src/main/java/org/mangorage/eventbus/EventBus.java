package org.mangorage.eventbus;

import org.mangorage.eventbus.interfaces.IEventState;
import org.mangorage.eventbus.interfaces.IListenerList;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

public class EventBus {
    private final Map<Class<?>, IListenerList<? extends IEventState>> listeners = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public <E, S extends IEventState> void addListener(EventKey<E, S> eventKey, BiConsumer<E, S> consumer) {
        IListenerList<S> list = (IListenerList<S>) listeners.computeIfAbsent(eventKey.eClass(), l -> eventKey.supplier().create(eventKey));
        list.register(consumer, eventKey);
    }

    public void post(Object object) {
        IListenerList<?> list = listeners.get(object.getClass());
        if (list != null)
            list.post(object);
    }


    public static void main(String[] args) {
        EventBus bus = new EventBus();

        bus.addListener(EventKey.EXAMPLE_EVENT, (o, s) -> {
            System.out.println("Got an event for " + o.getClass());
            System.out.println("With eventState of -> " + s.getClass());
        });

        bus.addListener(EventKey.EXAMPLE_EVENT, (o, s) -> {
            System.out.println("Got an event for " + o.getClass());
            System.out.println("With eventState of -> " + s.getClass());
        });


        bus.post(1);
    }
}

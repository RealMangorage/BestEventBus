package org.mangorage.eventbus;

import org.mangorage.eventbus.interfaces.IEventState;
import org.mangorage.eventbus.interfaces.IListenerList;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;

public class EventBus {
    private final Map<EventKey<?, ?>, IListenerList<? extends IEventState>> listeners = new ConcurrentHashMap<>();

    @SuppressWarnings("unchecked")
    public <E, S extends IEventState> void addListener(EventKey<E, S> eventKey, BiConsumer<E, S> consumer) {
        getListenerList(eventKey).register(consumer, eventKey);
    }

    public void post(Object object, EventKey<?, ?> eventKey) {
        IListenerList<?> list = getListenerList(eventKey);
        if (list != null) list.post(object);
    }

    public <S extends IEventState> IListenerList<S> getListenerList(EventKey<?, S> eventKey) {
        var list = listeners.get(eventKey);
        if (list != null) {
            return (IListenerList<S>) list;
        }

        if (eventKey.eClass() == Object.class) {
            return null;
        }

        return (IListenerList<S>) listeners.computeIfAbsent(eventKey, l -> eventKey.supplier().create(eventKey, getListenerList(eventKey.createNew(eventKey.eClass().getSuperclass()))));
    }


    public static void main(String[] args) {
        EventBus bus = new EventBus();

        bus.addListener(EventKey.EXAMPLE_EVENT_3, (o, s) -> {
            System.out.println("Got an event for " + o.getClass());
            System.out.println("With eventState of -> " + s.getClass());
            System.out.println("Number Listener");
        });

        bus.addListener(EventKey.EXAMPLE_EVENT, (o, s) -> {
            System.out.println("Got an event for " + o.getClass());
            System.out.println("With eventState of -> " + s.getClass());
        });

        bus.addListener(EventKey.EXAMPLE_EVENT_2, (o, s) -> {
            s.cancel();
            System.out.println("Got an event for " + o.getClass());
            System.out.println("With eventState of -> " + s.getClass());
        });

        bus.addListener(EventKey.EXAMPLE_EVENT_2, (o, s) -> {
            System.out.println("Got an event for " + o.getClass());
            System.out.println("With eventState of -> " + s.getClass());
        });

         // EventKey.EXAMPLE_EVENT.post(10, bus);
        EventKey.EXAMPLE_EVENT_2.post(10, bus);
        //EventKey.EXAMPLE_EVENT_3.post(10, bus);

    }
}

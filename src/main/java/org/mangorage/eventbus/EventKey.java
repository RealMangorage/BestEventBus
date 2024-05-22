package org.mangorage.eventbus;

import org.mangorage.eventbus.impl.AlternateEventListenerList;
import org.mangorage.eventbus.impl.AlternateEventState;
import org.mangorage.eventbus.impl.NormalEventState;
import org.mangorage.eventbus.impl.NormalEventListenerList;
import org.mangorage.eventbus.interfaces.IEventBus;
import org.mangorage.eventbus.interfaces.IEventState;
import org.mangorage.eventbus.interfaces.IListenerList;
import org.mangorage.eventbus.interfaces.IListenerListProvider;

import java.util.Objects;
import java.util.function.Supplier;

public record EventKey<E, S extends IEventState>(Class<E> eClass, Class<S> stateClass, IListenerListProvider<S> supplier) {
    public static final EventKey<Integer, NormalEventState> EXAMPLE_EVENT = new EventKey<>(Integer.class, NormalEventState.class, NormalEventListenerList::new);
    public static final EventKey<Number, NormalEventState> EXAMPLE_EVENT_3 = new EventKey<>(Number.class, NormalEventState.class, NormalEventListenerList::new);

    public static final EventKey<Integer, AlternateEventState> EXAMPLE_EVENT_2 = new EventKey<>(Integer.class, AlternateEventState.class, AlternateEventListenerList::new);


    public static <E, S extends IEventState> EventKey<E, S> create(Class<E> eClass, Class<S> stateClass, IListenerListProvider<S> supplier) {
        return new EventKey<>(eClass, stateClass, supplier);
    }


    public void post(E object, IEventBus bus) {
        bus.post(object, this);
    }

    public EventKey<E, S> createNew(Class<?> eClass) {
        return (EventKey<E, S>) new EventKey<>(eClass, stateClass, supplier);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eClass, stateClass);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        EventKey<?, ?> eventKey = (EventKey<?, ?>) obj;
        return Objects.equals(eClass, eventKey.eClass) && Objects.equals(stateClass, eventKey.stateClass);
    }
}

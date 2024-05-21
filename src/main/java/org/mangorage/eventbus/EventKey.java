package org.mangorage.eventbus;

import org.mangorage.eventbus.impl.AlternateEventListenerList;
import org.mangorage.eventbus.impl.AlternateEventState;
import org.mangorage.eventbus.impl.NormalEventState;
import org.mangorage.eventbus.impl.NormalEventListenerList;
import org.mangorage.eventbus.interfaces.IEventState;
import org.mangorage.eventbus.interfaces.IListenerList;
import org.mangorage.eventbus.interfaces.IListenerListProvider;

import java.util.Objects;
import java.util.function.Supplier;

public record EventKey<E, S extends IEventState>(Class<E> eClass, Class<S> stateClass, IListenerListProvider<S> supplier) {
    public static final EventKey<Integer, NormalEventState> EXAMPLE_EVENT = new EventKey<>(Integer.class, NormalEventState.class, NormalEventListenerList::new);
    public static final EventKey<Integer, AlternateEventState> EXAMPLE_EVENT_2 = new EventKey<>(Integer.class, AlternateEventState.class, AlternateEventListenerList::new);



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

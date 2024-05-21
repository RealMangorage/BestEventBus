package org.mangorage.eventbus;

import org.mangorage.eventbus.impl.NormalEventState;
import org.mangorage.eventbus.impl.NormalListenerList;
import org.mangorage.eventbus.interfaces.IEventState;
import org.mangorage.eventbus.interfaces.IListenerList;

import java.util.Objects;
import java.util.function.Supplier;

public record EventKey<E, S extends IEventState>(Class<E> eClass, Class<S> stateClass, Supplier<IListenerList<S>> supplier) {
    public static final EventKey<Integer, NormalEventState> EXAMPLE_EVENT = new EventKey<>(Integer.class, NormalEventState.class, NormalListenerList::new);

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

package org.mangorage.testbus;

import org.mangorage.eventbus.EventBus;
import org.mangorage.eventbus.EventKey;
import org.mangorage.eventbus.impl.NormalEventListenerList;
import org.mangorage.eventbus.impl.NormalEventState;

public class TestInheritance {

    public abstract static class BaseEvent {
        public final static class MySubEvent extends BaseEvent {}
        public final static class MyOtherSubEvent extends BaseEvent {}
    }

    public static final EventKey<BaseEvent, NormalEventState> BASE_EVENT = EventKey.create(
            BaseEvent.class,
            NormalEventState.class,
            NormalEventListenerList::new
    );

    public static final EventKey<BaseEvent.MySubEvent, NormalEventState> MY_SUB_EVENT = EventKey.create(
            BaseEvent.MySubEvent.class,
            NormalEventState.class,
            NormalEventListenerList::new
    );

    public static final EventKey<BaseEvent.MyOtherSubEvent, NormalEventState> MY_OTHER_SUB_EVENT = EventKey.create(
            BaseEvent.MyOtherSubEvent.class,
            NormalEventState.class,
            NormalEventListenerList::new
    );



    public static void main(String[] args) {
        var bus = EventBus.create();

        bus.addListener(BASE_EVENT, (o, s) -> {
            System.out.println("-------------------------------------------------------");
            System.out.println("Base Event Listener Got Event -> " + o + " With State -> " + s.getClass());
            System.out.println("-------------------------------------------------------");
        });

        bus.addListener(MY_SUB_EVENT, (o, s) -> {
            System.out.println("-------------------------------------------------------");
            System.out.println("My Sub Event Event Listener Got Event -> " + o + " With State -> " + s.getClass());
            System.out.println("-------------------------------------------------------");
        });

        bus.addListener(MY_OTHER_SUB_EVENT, (o, s) -> {
            System.out.println("-------------------------------------------------------");
            System.out.println("Ny Other Sub Event Listener Got Event -> " + o + " With State -> " + s.getClass());
            System.out.println("-------------------------------------------------------");
        });


        MY_OTHER_SUB_EVENT.post(new BaseEvent.MyOtherSubEvent(), bus);
    }
}

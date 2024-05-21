package org.mangorage.eventbus.impl;

import org.mangorage.eventbus.interfaces.IEventState;

public class AlternateEventState implements IEventState {
    private boolean cancelled = false;

    public void cancel() {
        this.cancelled = true;
    };
    public boolean isCancelled() {
        return cancelled;
    }
}

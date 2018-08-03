package ru.sdevteam.starlit.utils;

public abstract class StateMachine {
    private volatile int state;

    public StateMachine() {
        state = 0;
    }

    public int getState() {
        return state;
    }
    public boolean setState(int newState) {
        synchronized (this) {
            int oldState = state;
            if (onStateChanged(newState, oldState)) {
                state = newState;
                return true;
            }
            return false;
        }
    }

    protected abstract boolean onStateChanged(int newState, int oldState);
}

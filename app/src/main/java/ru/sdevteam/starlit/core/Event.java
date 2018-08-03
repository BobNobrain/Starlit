package ru.sdevteam.starlit.core;

public class Event {
    private boolean handled;
    public boolean isHandled() { return handled; }
    public void handle() { handled = true; }
}

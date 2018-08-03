package ru.sdevteam.starlit.core;

public class EventListenerAdapter implements IEventListener {
    @Override
    public void onTap(TapEvent ev) {}
    @Override
    public void onDoubleTap(TapEvent ev) {}
    @Override
    public void onLongTap(TapEvent ev) {}

    @Override
    public void onDragStart(DragEvent ev) {}
    @Override
    public void onDrag(DragEvent ev) {}
    @Override
    public void onDragEnd(DragEvent ev) {}
}

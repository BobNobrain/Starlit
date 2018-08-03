package ru.sdevteam.starlit.core;

public interface IEventListener {
    void onTap(TapEvent ev);
    void onDoubleTap(TapEvent ev);
    void onLongTap(TapEvent ev);

    void onDragStart(DragEvent ev);
    void onDrag(DragEvent ev);
    void onDragEnd(DragEvent ev);
}

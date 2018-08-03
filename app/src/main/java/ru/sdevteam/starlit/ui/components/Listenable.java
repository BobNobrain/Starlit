package ru.sdevteam.starlit.ui.components;

import java.util.ArrayList;
import java.util.List;

import ru.sdevteam.starlit.core.DragEvent;
import ru.sdevteam.starlit.core.GOComponent;
import ru.sdevteam.starlit.core.IEventListener;
import ru.sdevteam.starlit.core.TapEvent;

public class Listenable extends GOComponent {
    private List<IEventListener> listeners;
    public Listenable() {
        listeners = new ArrayList<>();
    }

    public void addListener(IEventListener l) {
        listeners.add(l);
    }
    public void removeListener(IEventListener l) {
        listeners.remove(l);
    }

    @Override
    public void onTap(TapEvent ev) {
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).onTap(ev);
        }
    }
    @Override
    public void onDoubleTap(TapEvent ev) {
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).onDoubleTap(ev);
        }
    }
    @Override
    public void onLongTap(TapEvent ev) {
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).onLongTap(ev);
        }
    }
    @Override
    public void onDragStart(DragEvent ev) {
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).onDragStart(ev);
        }
    }
    @Override
    public void onDrag(DragEvent ev) {
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).onDrag(ev);
        }
    }
    @Override
    public void onDragEnd(DragEvent ev) {
        for (int i = 0; i < listeners.size(); i++) {
            listeners.get(i).onDragEnd(ev);
        }
    }
}

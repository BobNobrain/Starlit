package ru.sdevteam.starlit.core;

import android.graphics.Canvas;

public class GOComponent extends EventListenerAdapter implements IHookHandler {
    protected GameObject go;
    protected boolean active;

    void setGameObject(GameObject gameObject) {
        go = gameObject;
    }
    protected GameObject gameObject() { return go; }

    // hooks
    public void update(float deltaTime) {}
    public void paint(Canvas c) {}

    @Override
    public boolean isActive() {
        return active;
    }

    public void launch() {}
}

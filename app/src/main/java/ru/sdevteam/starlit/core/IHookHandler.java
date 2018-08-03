package ru.sdevteam.starlit.core;

import android.graphics.Canvas;

public interface IHookHandler extends IEventListener {
    boolean isActive();
    void launch();
    void update(float dt);
    void paint(Canvas c);
    void stop();
}

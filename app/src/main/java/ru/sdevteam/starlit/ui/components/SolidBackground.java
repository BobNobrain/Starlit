package ru.sdevteam.starlit.ui.components;

import android.graphics.Canvas;

public class SolidBackground extends Colorable {
    public SolidBackground(int color) {
        super(color);
    }

    @Override
    public void paint(Canvas c) {
        c.drawRect(go.x, go.y, go.x+go.width, go.y+go.height, getPaint());
    }
}

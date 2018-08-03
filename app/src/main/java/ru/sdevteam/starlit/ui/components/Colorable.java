package ru.sdevteam.starlit.ui.components;

import android.graphics.Color;
import android.graphics.Paint;

import ru.sdevteam.starlit.core.GOComponent;
import ru.sdevteam.starlit.utils.Drawing;

public class Colorable extends GOComponent {
    public volatile int color;

    public Colorable(int color) {
        this.color = color;
    }

    private static Paint paint;
    protected Paint getPaint() {
        if (paint == null) {
            paint = new Paint();
        }
        paint.setColor(color);
        return paint;
    }

    public void setColor(int a, int r, int g, int b)
    {
        color = Color.argb(a, r, g, b);
    }
}

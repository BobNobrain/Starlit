package ru.sdevteam.starlit.ui.components;

import android.graphics.Canvas;
import android.graphics.Paint;

public class SolidBorder extends Colorable {
    public float width;
    public SolidBorder(int color, float borderWidth) {
        super(color);
        width = borderWidth;
    }

    @Override
    protected Paint getPaint() {
        Paint p = super.getPaint();
        p.setStrokeWidth(width);
        return p;
    }

    @Override
    public void paint(Canvas c) {
        float x = go.x, y = go.y, w = go.width, h = go.height;
        c.drawRect(x, y, x+w, y+h, getPaint());
    }
}

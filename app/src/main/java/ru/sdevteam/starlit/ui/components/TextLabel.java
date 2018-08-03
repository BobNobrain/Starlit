package ru.sdevteam.starlit.ui.components;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;

import ru.sdevteam.starlit.utils.Drawing;

public class TextLabel extends Colorable {
    public volatile int textSize;
    public String text;
    public Typeface font;

    public TextLabel(String label, int color) {
        super(color);
        text = label;
    }
    public TextLabel(String label) {
        this(label, Color.WHITE);
    }

    private static Paint textPaint;
    protected Paint getPaint(float size) {
        if (textPaint == null) {
            textPaint = new Paint();
            textPaint.setAntiAlias(true);
            textPaint.setTypeface(Drawing.mainFont);
            textPaint.setTextAlign(Paint.Align.CENTER);
        }
        textPaint.setColor(color);
        textPaint.setTextSize(size);
        textPaint.setTypeface(font);
        return textPaint;
    }
    @Override
    protected Paint getPaint() { return getPaint(textSize); }

    private Paint.FontMetrics fm;
    protected Paint.FontMetrics getMetrics()
    {
        if (fm == null)
        {
            fm = getPaint().getFontMetrics();
        }
        return fm;
    }

    @Override
    public void paint(Canvas c)
    {
        Paint.FontMetrics fm = getMetrics();
        float textHalfHeight = (fm.ascent + fm.descent)/2;
        float textHalfWidth = getPaint().measureText(text) / 2;
        c.drawText(text, go.x+go.width/2 - textHalfWidth, go.y+go.height/2 - textHalfHeight, getPaint());
    }
}

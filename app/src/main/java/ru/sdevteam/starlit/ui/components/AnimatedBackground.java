package ru.sdevteam.starlit.ui.components;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Region;

public class AnimatedBackground extends Colorable {
    private final float STRIPE_MIN_SPEED = 1.0F;
    private final float STRIPE_MAX_SPEED = 4.0F;

    private float stripeHeight = 30F, stripeY = -stripeHeight; // relative!
    private float linesGap = 10F, linesOffset = 0F;
    private float stripeSpeed = 0F, linesSpeed = 0.7F;
    private float lineHeight = 2F;

    public AnimatedBackground() {
        super(Color.WHITE);
    }

    @Override
    public void update(float deltaTime) {
        synchronized (this) {
            if(go.height < 3*stripeHeight) {
                stripeHeight = go.height / 3;
            }

            // stripe movement
            if (stripeSpeed != 0F) {
                stripeY += stripeSpeed;
                if (stripeY >= go.height + stripeHeight) {
                    // stripe has come across the whole component
                    stripeY = -stripeHeight;
                    stripeSpeed = 0;
                }
            } else {
                if (Math.random() < 0.003) {
                    // random test succeeded, launch the stripe
                    stripeSpeed = STRIPE_MIN_SPEED + (float) (Math.random()) * (STRIPE_MAX_SPEED - STRIPE_MIN_SPEED);
                }
            }

            // lines movement
            linesOffset += linesSpeed;
            if (linesOffset > linesGap) linesOffset -= linesGap;
        }
    }

    @Override
    public void paint(Canvas c) {
        Paint fp = getPaint(); // assumed to be non-opaque

        float x = go.x, y = go.y, w = go.width, h = go.height;

        Rect old = c.getClipBounds();
        c.clipRect(x, y, x + w, y + h, Region.Op.INTERSECT);
        synchronized (this) {
            for (float i = y + linesOffset; i < y + h; i += linesGap) {
                c.drawRect(x, i, x + w, i+lineHeight, fp);
            }
            c.drawRect(x, y + stripeY, x + w, y + stripeY + stripeHeight, fp);
        }
        c.clipRect(old, Region.Op.REPLACE);
    }
}

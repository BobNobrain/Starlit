package ru.sdevteam.starlit.ui;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Region;

/**
 * Created by user on 15.08.2016.
 */
public class DynamicFoneComponent extends UIComponent
{
	private final float STRIPE_MIN_SPEED = 1.0F;
	private final float STRIPE_MAX_SPEED = 4.0F;

	private float stripeHeight = 30F, stripeY = -stripeHeight; // relative!
	private float linesGap = 10F, linesOffset = 0F;
	private float stripeSpeed = 0F, linesSpeed = 0.7F;
	private float lineHeight = 2F;

	public DynamicFoneComponent(int x, int y, int width, int height)
	{
		super(x, y, width, height);
		if(height < 3*stripeHeight)
		{
			stripeHeight = height/3;
		}
	}
	public DynamicFoneComponent(int x, int y, int width, int height, String text)
	{
		super(x, y, width, height, text);
	}

	@Override
	protected final void paintFone(Canvas c)
	{
		super.paintFone(c);
		Paint fp = getFonePaint(); // assumed to be non-opaque

		Rect old = c.getClipBounds();
		c.clipRect(x, y, x + width, y + height);
		synchronized (this)
		{
			for (float i = y + linesOffset; i < y + height; i += linesGap)
			{
				c.drawRect(x, i, x + width, i+lineHeight, fp);
			}
			c.drawRect(x, y + stripeY, x + width, y + stripeY + stripeHeight, fp);
		}
		c.clipRect(old, Region.Op.REPLACE);
		c.drawRect(x, y, x+width, y+height, getBorderPaint());
	}

	@Override
	public void update()
	{
		super.update();

		synchronized (this)
		{
			// stripe movement
			if (stripeSpeed != 0F)
			{
				stripeY += stripeSpeed;
				if (stripeY >= height + stripeHeight)
				{
					// stripe has come across the whole component
					stripeY = -stripeHeight;
					stripeSpeed = 0;
				}
			}
			else
			{
				if (Math.random() < 0.003)
				{
					// random test succeeded, launch the stripe
					stripeSpeed = STRIPE_MIN_SPEED + (float) (Math.random()) * (STRIPE_MAX_SPEED - STRIPE_MIN_SPEED);
				}
			}

			// lines movement
			linesOffset += linesSpeed;
			if (linesOffset > linesGap) linesOffset -= linesGap;
		}
	}
}

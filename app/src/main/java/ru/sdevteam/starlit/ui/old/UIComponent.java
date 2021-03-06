package ru.sdevteam.starlit.ui.old;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import ru.sdevteam.starlit.utils.Drawing;

import java.util.Vector;

/**
 * Created by user on 14.07.2016.
 */
public abstract class UIComponent
{
	private volatile int x, y, width, height;
	private volatile int textSize;

	public void locate(int nx, int ny) { x = nx; y = ny; }
	public void resize(int nw, int nh) { width = nw; height = nh; }
	public int getX() { return x; }
	public int getY() { return y; }
	public int getWidth() { return width; }
	public int getHeight() { return height; }

	public int getTextSize() { return textSize; }
	public void setTextSize(int val) { textSize = val; }

	private String text;
	public String getText() { return text; }
	public void setText(String val) { text = val; }

	private Typeface font;
	public Typeface getFont() { return font; }
	public void setFont(Typeface font) { this.font = font; }

	protected volatile int textColor, bgColor;

	private static Paint textPaint;
	protected Paint getTextPaint(float size)
	{
		if (textPaint == null)
		{
			textPaint = new Paint();
			textPaint.setAntiAlias(true);
			textPaint.setTypeface(Drawing.mainFont);
			textPaint.setTextAlign(Paint.Align.CENTER);
		}
		textPaint.setColor(textColor);
		textPaint.setTextSize(size);
		textPaint.setTypeface(this.font);
		return textPaint;
	}
	protected Paint getTextPaint() { return getTextPaint(textSize); }

	private Paint fonePaint;
	protected Paint getFonePaint()
	{
		if (fonePaint == null)
		{
			fonePaint = new Paint();
		}
		fonePaint.setColor(bgColor);
		return fonePaint;
	}
	public void setBackgroundColor(int a, int r, int g, int b)
	{
		bgColor = Color.argb(a, r, g, b);
	}
	public void setTextColor(int a, int r, int g, int b)
	{
		textColor = Color.argb(a, r, g, b);
	}

	private Paint borderPaint;
	protected float borderWidth = 1;
	protected Paint getBorderPaint()
	{
		if (borderPaint == null)
		{
			borderPaint = new Paint();
		}
		borderPaint.setColor(textColor);
		borderPaint.setStyle(Paint.Style.STROKE);
		borderPaint.setStrokeWidth(borderWidth);
		return borderPaint;
	}

	public UIComponent()
	{
		textColor = Color.WHITE;
		bgColor = Color.argb(32, 255, 255, 255);
		textSize = 20;
		text = "";
		font = Drawing.mainFont;

		listeners = new Vector<>();
	}
	public UIComponent(int w, int h)
	{
		this();
		width = w; height = h;
	}
	public UIComponent(int x, int y, int w, int h)
	{
		this();
		this.x = x; this.y = y; this.height = h; this.width = w;
	}
	public UIComponent(int x, int y, int w, int h, String text)
	{
		this(x, y, w, h);
		this.text = text;
	}


	public void paint(Canvas c)
	{
		paintFone(c);
		paintText(c);
	}

	protected void paintFone(Canvas c)
	{
		c.drawRect(x, y, x+width, y+height, getFonePaint());
	}

	private Paint.FontMetricsInt fm;
	protected Paint.FontMetricsInt getMetrics()
	{
		if (fm == null)
		{
			fm = getTextPaint().getFontMetricsInt();
		}
		return fm;
	}
	protected void paintText(Canvas c)
	{
		getMetrics();
		int textHalfHeight = (fm.ascent + fm.descent)/2;
		int textHalfWidth = (int) getTextPaint().measureText(text) / 2;
		c.drawText(text, x+width/2 - textHalfWidth, y+height/2 - textHalfHeight, getTextPaint());
	}

	public void update() {}


	public boolean is(Class T)
	{
		return T.isAssignableFrom(getClass());
	}


	//
	// EVENTS
	//

	private Vector<EventListener> listeners;
	public void subscribe(EventListener l) { listeners.add(l); }
	public void unsubscribe(EventListener l) { listeners.remove(l); }

	public boolean invokeOnTap(int tapX, int tapY)
	{
		boolean consumed = false;
		for (EventListener l : listeners)
		{
			if(l != null)
				if(l.onTap(tapX, tapY)) consumed = true;
		}
		return consumed;
	}

	public boolean invokeOnDoubleTap(int tapX, int tapY)
	{
		boolean consumed = false;
		for (EventListener l : listeners)
		{
			if(l != null)
				if(l.onDoubleTap(tapX, tapY)) consumed = true;
		}
		return consumed;
	}

	public boolean invokeOnLongTap(int tapX, int tapY)
	{
		boolean consumed = false;
		for (EventListener l : listeners)
		{
			if(l != null)
				if(l.onLongTap(tapX, tapY)) consumed = true;
		}
		return consumed;
	}

	public boolean invokeOnScroll(int sx, int sy, int dx, int dy)
	{
		boolean consumed = false;
		for (EventListener l : listeners)
		{
			if(l != null)
				if(l.onScroll(dx, dy)) consumed = true;
		}
		return consumed;
	}


	public interface EventListener
	{
		boolean onTap(int tapX, int tapY);
		boolean onDoubleTap(int tapX, int tapY);
		boolean onLongTap(int tapX, int tapY);
		boolean onScroll(int dx, int dy);
	}

	public class EventListenerAdapter implements EventListener
	{

		@Override
		public boolean onTap(int tapX, int tapY)
		{
			return false;
		}

		@Override
		public boolean onDoubleTap(int tapX, int tapY)
		{
			return false;
		}

		@Override
		public boolean onLongTap(int tapX, int tapY)
		{
			return false;
		}

		@Override
		public boolean onScroll(int dx, int dy)
		{
			return false;
		}
	}
}

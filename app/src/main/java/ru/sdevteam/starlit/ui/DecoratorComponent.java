package ru.sdevteam.starlit.ui;

import android.graphics.Canvas;

/**
 * Class decorates any UI component. Used for overriding all needed methods and properties
 */
public class DecoratorComponent extends UIComponent
{
	protected UIComponent decorated;

	public DecoratorComponent(UIComponent of)
	{
		decorated = of;

		borderWidth = decorated.borderWidth;
		bgColor = decorated.bgColor;
		textColor = decorated.textColor;
		setTextSize(decorated.getTextSize());
	}

	@Override
	public boolean is(Class T)
	{
		if (T.isAssignableFrom(getClass()))
		{
			return true;
		}
		if (decorated instanceof DecoratorComponent)
		{
			return decorated.is(T);
		}
		else
		{
			return T.isAssignableFrom(decorated.getClass());
		}
	}
	public UIComponent findOfType(Class T)
	{
		if (T.isAssignableFrom(getClass()))
		{
			return this;
		}
		if (T.isAssignableFrom(decorated.getClass()))
		{
			return decorated;
		}
		if (decorated instanceof DecoratorComponent)
		{
			return ((DecoratorComponent) decorated).findOfType(T);
		}
		return null;
	}

//	@Override
//	protected void paintFone(Canvas c)
//	{
//		decorated.paintFone(c);
//	}
//	@Override
//	protected void paintText(Canvas c)
//	{
//		decorated.paintText(c);
//	}


	@Override
	public void paint(Canvas c)
	{
		decorated.paint(c);
	}

	@Override
	public void setBackgroundColor(int a, int r, int g, int b)
	{
		super.setBackgroundColor(a, r, g, b);
		decorated.setBackgroundColor(a, r, g, b);
	}
	@Override
	public void setTextColor(int a, int r, int g, int b)
	{
		super.setTextColor(a, r, g, b);
		decorated.setTextColor(a, r, g, b);
	}

	@Override
	public void update()
	{
		decorated.update();
	}

	@Override
	public int getX() { return decorated.getX(); }
	@Override
	public int getY() { return decorated.getY(); }
	@Override
	public int getWidth() { return decorated.getWidth(); }
	@Override
	public int getHeight() { return decorated.getHeight(); }

	@Override
	public void locate(int nx, int ny) { decorated.locate(nx, ny); }
	@Override
	public void resize(int nw, int nh) { decorated.resize(nw, nh); }

	@Override
	public int getTextSize() { return decorated.getTextSize(); }
	@Override
	public void setTextSize(int val) { decorated.setTextSize(val); }

	@Override
	public String getText() { return decorated.getText(); }
	@Override
	public void setText(String val) { decorated.setText(val); }

	@Override
	public void subscribe(EventListener l)
	{
		decorated.subscribe(l);
	}
	@Override
	public void unsubscribe(EventListener l)
	{
		decorated.unsubscribe(l);
	}

	@Override
	public boolean invokeOnTap(int tapX, int tapY)
	{
		return decorated.invokeOnTap(tapX, tapY);
	}
	@Override
	public boolean invokeOnDoubleTap(int tapX, int tapY)
	{
		return decorated.invokeOnDoubleTap(tapX, tapY);
	}
	@Override
	public boolean invokeOnLongTap(int tapX, int tapY)
	{
		return decorated.invokeOnLongTap(tapX, tapY);
	}
	@Override
	public boolean invokeOnScroll(int sx, int sy, int dx, int dy)
	{
		return decorated.invokeOnScroll(sx, sy, dx, dy);
	}
}

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

		x = of.x;
		y = of.y;
		width = of.width;
		height = of.height;

		borderWidth = decorated.borderWidth;
		bgColor = decorated.bgColor;
		textColor = decorated.textColor;
		textSize = decorated.textSize;
		text = decorated.text;
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
			return ((DecoratorComponent) decorated).is(T);
		}
		return false;
	}
	public UIComponent findOfType(Class T)
	{
		if (T.isAssignableFrom(getClass()))
		{
			return this;
		}
		if (decorated instanceof DecoratorComponent)
		{
			return ((DecoratorComponent) decorated).findOfType(T);
		}
		return null;
	}

	@Override
	protected void paintFone(Canvas c)
	{
		decorated.paintFone(c);
	}
	@Override
	protected void paintText(Canvas c)
	{
		decorated.paintText(c);
	}

	@Override
	public void setBackgroundColor(int a, int r, int g, int b)
	{
		decorated.setBackgroundColor(a, r, g, b);
	}
	@Override
	public void setTextColor(int a, int r, int g, int b)
	{
		decorated.setTextColor(a, r, g, b);
	}

	@Override
	public void update()
	{
		decorated.update();
	}

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
	public boolean invokeOnScroll(int dx, int dy)
	{
		return decorated.invokeOnScroll(dx, dy);
	}
}

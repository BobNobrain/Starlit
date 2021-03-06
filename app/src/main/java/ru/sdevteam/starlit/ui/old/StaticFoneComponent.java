package ru.sdevteam.starlit.ui.old;

import android.graphics.Canvas;

/**
 * Class represents a component with simple static background
 */
public class StaticFoneComponent extends DecoratorComponent
{
	public StaticFoneComponent(UIComponent of)
	{
		super(of);
		setBackgroundColor(32, 255, 255, 255);
	}

	@Override
	public void paint(Canvas c)
	{
		c.drawRect(
			decorated.getX(),
			decorated.getY(),
			decorated.getX() + decorated.getWidth(),
			decorated.getY() + decorated.getHeight(),
			getFonePaint()
		);

		decorated.paint(c);
	}
}

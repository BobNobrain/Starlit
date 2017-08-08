package ru.sdevteam.starlit.ui;

import android.graphics.Canvas;

/**
 * Class represents a component with simple static background
 */
public class StaticFoneComponent extends DecoratorComponent
{
	public StaticFoneComponent(UIComponent of)
	{
		super(of);
		setBackgroundColor(64, 255, 255, 255);
	}

	@Override
	protected void paintFone(Canvas c)
	{
		c.drawRect(
			decorated.getX(),
			decorated.getY(),
			decorated.getX() + decorated.getWidth(),
			decorated.getY() + decorated.getHeight(),
			getFonePaint()
		);
	}
}

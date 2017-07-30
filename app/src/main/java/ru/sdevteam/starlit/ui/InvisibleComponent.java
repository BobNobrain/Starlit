package ru.sdevteam.starlit.ui;

import android.graphics.Canvas;

/**
 * Class represents invisible and non-interactive ("fake") component
 */
public class InvisibleComponent extends UIComponent
{
	public InvisibleComponent()
	{
		super();
	}

	@Override
	public void paint(Canvas c)
	{
		// do nothing
	}

	@Override
	public boolean invokeOnTap(int tapX, int tapY)
	{
		return false;
	}

	@Override
	public boolean invokeOnDoubleTap(int tapX, int tapY)
	{
		return false;
	}

	@Override
	public boolean invokeOnLongTap(int tapX, int tapY)
	{
		return false;
	}

	@Override
	public boolean invokeOnScroll(int dx, int dy)
	{
		return false;
	}
}

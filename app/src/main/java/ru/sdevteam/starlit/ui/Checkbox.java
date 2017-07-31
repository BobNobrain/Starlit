package ru.sdevteam.starlit.ui;

import android.graphics.Canvas;
import android.graphics.Color;

import java.util.Vector;

/**
 * Represents toggleable UIComponent. Decorates another UIComponent to make checkbox based on it.
 */
public class Checkbox extends DecoratorComponent
{
	private boolean active;

	private int bgColorOn, textColorOff;

	protected Vector<StateChangedListener> stateListeners;

	public Checkbox(UIComponent of)
	{
		super(of);
		active = false;

		makeBgColorOn();
		makeTextColorOff();

		toggle(false);

		stateListeners = new Vector<>();
	}

	@Override
	public void setBackgroundColor(int a, int r, int g, int b)
	{
		super.setBackgroundColor(a, r, g, b);
		makeBgColorOn();
	}
	private void makeBgColorOn()
	{
		bgColorOn = Color.argb(
			Math.min(Color.alpha(bgColor) * 2, 255),
			Color.red(bgColor),
			Color.green(bgColor),
			Color.blue(bgColor)
		);
	}

	@Override
	public void setTextColor(int a, int r, int g, int b)
	{
		super.setTextColor(a, r, g, b);
		makeTextColorOff();
	}
	private void makeTextColorOff()
	{
		textColorOff = Color.argb(
			Color.alpha(textColor) / 2,
			Color.red(textColor),
			Color.green(textColor),
			Color.blue(textColor)
		);
	}

	@Override
	public boolean invokeOnTap(int tapX, int tapY)
	{
		super.invokeOnTap(tapX, tapY);
		setState(!active);
		return true;
	}

	protected void toggle(boolean newState)
	{
		this.active = newState;
		if (newState)
		{
//			decorated.borderWidth = borderWidth * 1.5F;
			decorated.bgColor = bgColorOn;
			decorated.textColor = textColor;
		}
		else
		{
			decorated.bgColor = bgColor;
			decorated.textColor = textColorOff;
//			decorated.borderWidth = borderWidth;
		}
	}

	protected void invokeOnStateChange(boolean newState)
	{
		for (int i = 0; i < stateListeners.size(); i++)
		{
			stateListeners.get(i).onStateChanged(newState);
		}
	}

	public void subscribeStateChange(StateChangedListener l)
	{
		stateListeners.add(l);
	}
	public void unsubscribeStateChange(StateChangedListener l)
	{
		stateListeners.remove(l);
	}

	public void setState(boolean checked)
	{
		toggle(checked);
		invokeOnStateChange(checked);
	}
	public boolean getState()
	{
		return active;
	}

	public interface StateChangedListener
	{
		void onStateChanged(boolean newState);
	}
}

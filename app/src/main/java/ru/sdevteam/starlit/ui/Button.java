package ru.sdevteam.starlit.ui;

import android.graphics.Color;
import ru.sdevteam.starlit.utils.MathUtils;

/**
 * Created by bob on 30.07.17.
 */
public class Button extends DynamicFoneComponent
{
	private int sourceBg, disabledBg;
	protected int maxTicks = 15;
	protected boolean enabled = true;

	public Button(int x, int y, int width, int height, String text)
	{
		super(x, y, width, height, text);
		sourceBg = bgColor;
		makeDisabledBg();
	}

	private void makeDisabledBg()
	{
		int sum = Color.red(bgColor) + Color.green(bgColor) + Color.blue(bgColor);
		disabledBg = Color.argb(Color.alpha(bgColor), sum/5, sum/5, sum/5);
	}

	@Override
	public void setBackgroundColor(int a, int r, int g, int b)
	{
		super.setBackgroundColor(a, r, g, b);
		sourceBg = bgColor;
		makeDisabledBg();
	}

	@Override
	public boolean invokeOnTap(int tapX, int tapY)
	{
		if (!enabled) return true;
		ticks = maxTicks;
		return super.invokeOnTap(tapX, tapY);
	}

	public void setEnabled(boolean value)
	{
		enabled = value;
		if (enabled)
		{
			bgColor = sourceBg;
		}
		else
		{
			bgColor = disabledBg;
			ticks = 0;
		}
	}

	private int ticks;
	@Override
	public void update()
	{
		super.update();
		if (ticks > 0)
		{
			--ticks;
			super.setBackgroundColor(
				MathUtils.towards(Color.alpha(sourceBg), 255, ticks, maxTicks),
				MathUtils.towards(Color.red(sourceBg), 255, ticks, maxTicks),
				MathUtils.towards(Color.green(sourceBg), 255, ticks, maxTicks),
				MathUtils.towards(Color.blue(sourceBg), 255, ticks, maxTicks)
			);
		}
	}
}

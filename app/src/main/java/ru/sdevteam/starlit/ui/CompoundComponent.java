package ru.sdevteam.starlit.ui;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Region;
import ru.sdevteam.starlit.utils.MathUtils;

import java.util.Vector;

/**
 * Class represents a container component. It holds many UI components inside and controls them.
 */
public class CompoundComponent extends UIComponent
{
	protected Vector<UIComponent> children;
	public Vector<UIComponent> getChildren()
	{
		return children;
	}

	public CompoundComponent(int x, int y, int width, int height)
	{
		super(x, y, width, height);
		children = new Vector<>();
		bgColor = Color.TRANSPARENT;
	}

	public void appendChild(UIComponent child) { children.add(child); }
	public void removeChild(UIComponent child) { children.remove(child); }

	public void setTextSizeForAll(int val)
	{
		setTextSize(val);
		for (UIComponent c: children)
		{
			c.setTextSize(val);
			if (c instanceof CompoundComponent)
			{
				((CompoundComponent) c).setTextSizeForAll(val);
			}
			if (c instanceof DecoratorComponent)
			{
				DecoratorComponent d = (DecoratorComponent) c;
				if (d.is(CompoundComponent.class))
				{
					((CompoundComponent) d.findOfType(CompoundComponent.class)).setTextSizeForAll(val);
				}
			}
		}
	}

	@Override
	public boolean invokeOnTap(int tapX, int tapY)
	{
		super.invokeOnTap(tapX, tapY);
		for (int i = children.size()-1; i >= 0; i--)
		{
			UIComponent child = children.get(i);
			if(MathUtils.pointInRect(tapX, tapY, child.getX(), child.getY(), child.getWidth(), child.getHeight()))
			{
				if(child.invokeOnTap(tapX, tapY)) return true;
			}
		}
		return false;
	}

	@Override
	public boolean invokeOnDoubleTap(int tapX, int tapY)
	{
		super.invokeOnDoubleTap(tapX, tapY);
		for (int i = children.size()-1; i >= 0; i--)
		{
			UIComponent child = children.get(i);
			if(MathUtils.pointInRect(tapX, tapY, child.getX(), child.getY(), child.getWidth(), child.getHeight()))
			{
				if(child.invokeOnDoubleTap(tapX, tapY)) return true;
			}
		}
		return false;
	}

	@Override
	public boolean invokeOnLongTap(int tapX, int tapY)
	{
		super.invokeOnLongTap(tapX, tapY);
		for (int i = children.size()-1; i >= 0; i--)
		{
			UIComponent child = children.get(i);
			if(MathUtils.pointInRect(tapX, tapY, child.getX(), child.getY(), child.getWidth(), child.getHeight()))
			{
				if(child.invokeOnLongTap(tapX, tapY)) return true;
			}
		}
		return false;
	}

	@Override
	public boolean invokeOnScroll(int sx, int sy, int dx, int dy)
	{
		super.invokeOnScroll(sx, sy, dx, dy);
		for (int i = children.size()-1; i >= 0; i--)
		{
			UIComponent child = children.get(i);
			if(MathUtils.pointInRect(sx, sy, child.getX(), child.getY(), child.getWidth(), child.getHeight()))
			{
				if(child.invokeOnScroll(sx, sy, dx, dy)) return true;
			}
		}
		return false;
	}

	@Override
	public void paint(Canvas c)
	{
		super.paint(c);
		for (int i = 0; i < children.size(); i++)
		{
			children.get(i).paint(c);
		}
//		c.drawRect(getX(), getY(), getX() + getWidth(), getY() + getHeight(), getBorderPaint());
	}

	@Override
	public void update()
	{
		super.update();
		for (int i = 0; i < children.size(); i++)
		{
			children.get(i).update();
		}
	}
}

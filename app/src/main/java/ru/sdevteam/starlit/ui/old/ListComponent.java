package ru.sdevteam.starlit.ui.old;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Region;

/**
 * Component represents a vertical scrollable list
 */
public class ListComponent extends CompoundComponent
{
	// after listStart goes padding and only after it goes content
	private int listStart, contentHeightWithPadding;
	private int vx;
	private boolean stretchItems;
	private int padding;

	public ListComponent(int x, int y, int width, int height, int padding, boolean stretchItems)
	{
		super(x, y, width, height);
		this.stretchItems = stretchItems;
		this.padding = padding;
		listStart = y;
		contentHeightWithPadding = 2*padding;
	}

	@Override
	public void locate(int nx, int ny)
	{
		int dy = ny - getY();
		super.locate(nx, ny);
		listStart += dy;
		updatePositions();
	}

	@Override
	public void appendChild(UIComponent child)
	{
		int cy = listStart + padding;
		if (children.size() > 0)
		{
			UIComponent lc = children.get(children.size() - 1);
			cy = lc.getY() + lc.getHeight();
		}
		super.appendChild(child);
		child.locate(getX() + padding, cy);
		if (stretchItems)
			child.resize(getWidth() - padding*2, child.getHeight());
		contentHeightWithPadding += child.getHeight();
	}

	@Override
	public void removeChild(UIComponent child)
	{
		super.removeChild(child);
		contentHeightWithPadding -= child.getHeight();
		checkBounds();
		updatePositions();
	}

	public void clear()
	{
		children.clear();
		contentHeightWithPadding = 2*padding;
		listStart = getY();
	}

	@Override
	public boolean invokeOnScroll(int sx, int sy, int dx, int dy)
	{
		// no scrolling if there is no enough items in list
		if (contentHeightWithPadding > getHeight())
		{
			listStart -= dy;
			checkBounds();
			updatePositions();
		}
		super.invokeOnScroll(sx, sy, dx, dy);
		return true;
	}

	private void updatePositions()
	{
		int penY = listStart + padding, penX = getX() + padding;
		for (int i = 0; i < children.size(); i++)
		{
			UIComponent child = children.get(i);
			child.locate(penX, penY);
			penY += child.getHeight();
		}
//		contentHeightWithPadding = penY;
	}

	private void checkBounds()
	{
		if (listStart + contentHeightWithPadding < getY() + getHeight())
		{
			listStart = getY() + getHeight() - contentHeightWithPadding;
		}
		if (listStart > getY())
		{
			listStart = getY();
		}
	}

	@Override
	public void paint(Canvas c)
	{
		Rect old = c.getClipBounds();
		c.clipRect(getX(), getY(), getX() + getWidth(), getY() + getHeight(), Region.Op.INTERSECT);
		super.paint(c);
		c.clipRect(old, Region.Op.REPLACE);
	}
}

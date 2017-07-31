package ru.sdevteam.starlit.ui;

/**
 * Class allows to align components inside container to a grid
 */
public class GridContainer extends CompoundComponent
{
	protected int cellW, cellH, marginX, marginY, cellsX, cellsY;

	public GridContainer(int x, int y, int width, int height)
	{
		super(x, y, width, height);
		cellW = width;
		cellH = height;
		cellsX = cellsY = 1;
	}

	public void setGrid(int cellsX, int cellsY, int marginX, int marginY)
	{
		cellW = (width - (cellsX - 1) * marginX) / cellsX;
		cellH = (height - (cellsY - 1) * marginY) / cellsY;
		this.marginX = marginX;
		this.marginY = marginY;
		this.cellsX = cellsX;
		this.cellsY = cellsY;
	}

	public void setCellSize(int cellW, int cellH, int mx, int my)
	{
		this.cellW = cellW;
		this.cellH = cellH;
		marginY = mx;
		marginY = my;
		cellsX = (width + marginX) / (cellW + marginX);
		cellsY = (height + marginY) / (cellH + marginY);
	}

	public void appendToGrid(UIComponent child, int cx, int cy)
	{
		appendChild(child);
		child.x = x + cx * (cellW + marginX);
		child.y = y + cy * (cellH + marginY);
	}
	public void stretchToGrid(UIComponent child, int cx, int cy)
	{
		appendToGrid(child, cx, cy);
		child.width = cellW;
		child.height = cellH;
	}

	public int getCellsX() { return cellsX; }
	public int getCellsY() { return cellsY; }
}

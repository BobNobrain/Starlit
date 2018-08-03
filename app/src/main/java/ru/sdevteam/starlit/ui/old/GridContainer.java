package ru.sdevteam.starlit.ui.old;

/**
 * Class allows to align components inside container to a grid
 */
@Deprecated
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

	/**
	 * Sets grid with given square cell size and equal margins
	 * @param cellsY cell sizes
	 * @param margin margins between cells
	 */
	public void setGrid(int cellsY, int margin)
	{
		this.marginX = margin;
		this.marginY = margin;
		cellW = cellH = (getHeight() - (cellsY - 1) * marginY) / cellsY;
		this.cellsY = cellsY;
		this.cellsX = (getWidth() + marginX) / (cellW + marginX);
	}

//	public void setGrid(int cellsX, int cellsY, int marginX, int marginY)
//	{
//		cellW = (width - (cellsX - 1) * marginX) / cellsX;
//		cellH = (height - (cellsY - 1) * marginY) / cellsY;
//		this.marginX = marginX;
//		this.marginY = marginY;
//		this.cellsX = cellsX;
//		this.cellsY = cellsY;
//	}
//
//	public void setCellSize(int cellW, int cellH, int mx, int my)
//	{
//		this.cellW = cellW;
//		this.cellH = cellH;
//		marginY = mx;
//		marginY = my;
//		cellsX = (width + marginX) / (cellW + marginX);
//		cellsY = (height + marginY) / (cellH + marginY);
//	}

	public void appendToGrid(UIComponent child, int cx, int cy)
	{
		appendChild(child);
		child.locate(getX() + cx * (cellW + marginX), getY() + cy * (cellH + marginY));
	}
	public void stretchToGrid(UIComponent child, int cx, int cy)
	{
		stretchToGrid(child, cx, cy, 1, 1);
	}
	public void stretchToGrid(UIComponent child, int cx, int cy, float cw, float ch)
	{
		appendToGrid(child, cx, cy);
		child.resize((int) ((cellW + marginX) * cw) - marginX, (int) ((cellH + marginY) * ch) - marginY);
	}

	public int getCellsX() { return cellsX; }
	public int getCellsY() { return cellsY; }
}

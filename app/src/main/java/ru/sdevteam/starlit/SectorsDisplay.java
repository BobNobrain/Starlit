package ru.sdevteam.starlit;

import android.graphics.*;
import ru.sdevteam.starlit.ui.old.GameUI;
import ru.sdevteam.starlit.utils.MathUtils;
import ru.sdevteam.starlit.world.Sector;
import ru.sdevteam.starlit.world.Star;
import ru.sdevteam.starlit.world.World;

/**
 * Created by user on 26.06.2016.
 */
public class SectorsDisplay extends AbstractDisplay
{
	public static final int SPACE_COLOR = Color.BLACK;
	private static final int EDGE_COLOR = Color.rgb(16, 16, 16);
	private static final int INITIAL_VP_WIDTH = 900;
	public static final int STAR_BORDER_COLOR = Color.rgb(32, 32, 32);


	private World world;
	private Star selected;

	private Paint sectorEdgePaint;

	private float sectorScreenWidth;

	private Sector[] visible;


	public SectorsDisplay(World displaying, int displayWidth, int displayHeight, GameUI gameUI)
	{
		super(displayWidth, displayHeight, gameUI);

		world = displaying;

		// scale coeff aligns display width as world viewport width
		defineScaleCoeff(displayWidth, INITIAL_VP_WIDTH);

		sectorScreenWidth = Sector.SECTOR_SIZE / screenToWorldScale;

		borderPaint.setStrokeWidth(iconSize/2);

		sectorEdgePaint = new Paint();
		sectorEdgePaint.setColor(EDGE_COLOR);
		sectorEdgePaint.setStyle(Paint.Style.STROKE);
		sectorEdgePaint.setStrokeWidth(selectionPaint.getStrokeWidth());

		// (temp) placing viewport into center of sector
		vpY = vpX = Sector.SECTOR_SIZE/2;
		// TODO: center it on home star

		moveViewportBy(0, 0);
	}

	@Override
	public void drawContent(Canvas c)
	{
		c.drawColor(SPACE_COLOR);

//		Sector[] visible = world.getVisibleSectors(vpX, vpY);

		float 	gridX = worldX2Screen(visible[0].getX()*Sector.SECTOR_SIZE + World.ORIGIN_X),
				gridY = worldY2Screen(visible[0].getY()*Sector.SECTOR_SIZE + World.ORIGIN_Y);
		// drawing grid
		c.drawLine(gridX, gridY, gridX+2*sectorScreenWidth, gridY, sectorEdgePaint);
		c.drawLine(gridX, gridY+sectorScreenWidth,
				gridX+2*sectorScreenWidth, gridY+sectorScreenWidth, sectorEdgePaint);
		c.drawLine(gridX, gridY+2*sectorScreenWidth,
				gridX+2*sectorScreenWidth, gridY+2*sectorScreenWidth, sectorEdgePaint);
		c.drawLine(gridX, gridY, gridX, gridY+2*sectorScreenWidth, sectorEdgePaint);
		c.drawLine(gridX+sectorScreenWidth, gridY,
				gridX+sectorScreenWidth, gridY+2*sectorScreenWidth, sectorEdgePaint);
		c.drawLine(gridX+2*sectorScreenWidth, gridY,
				gridX+2*sectorScreenWidth, gridY+2*sectorScreenWidth, sectorEdgePaint);

		for (Sector s : visible)
		{
			drawSector(s, c);
		}

		if(selected != null)
		{
			float sx = worldX2Screen(selected.getWorldX()),
					sy = worldY2Screen(selected.getWorldY());
			drawSelectionAround(sx,	sy, c, selectionPaint);
			drawPlanetsAround(selected.getOrbitDistances().length, sx, sy, c, selectionPaint);
		}

		// drawing status text
//		drawStatusText(String.format("Sector %1$s", visible[0].getName()), c);
	}

	private void drawSector(Sector s, Canvas c)
	{
		Star[] stars = s.getStarsArray();
		for(int i = 0; i<stars.length; i++)
		{
			if(stars[i]==null) break;
			drawStarSystem(stars[i], c);
		}
	}
	private void drawStarSystem(Star s, Canvas c)
	{
		float	screenX = worldX2Screen(s.getWorldX()),
				screenY = worldY2Screen(s.getWorldY());

		c.save();
		c.translate(screenX, screenY);
		c.scale(worldToScreenScale, worldToScreenScale);
		c.drawCircle(0, 0, s.getType().size, s.getType().starPaint);
		c.restore();
		drawSelectionAround(screenX, screenY, c, borderPaint);
		drawPlanetsAround(s.getOrbitDistances().length, screenX, screenY, c, borderPaint);
		drawResourceIcons(screenX, screenY, s, s==selected, c);
		drawName(screenX, screenY, s.getName(), c);
	}

	private void drawPlanetsAround(int planetsCount, float screenX, float screenY, Canvas c, Paint p)
	{
		float angle, x, y;
		Paint.Style old = p.getStyle();
		p.setStyle(Paint.Style.FILL);
		for(int i=0; i<planetsCount; i++)
		{
			angle = -2*(float)Math.PI / 3 + (float)i/planetsCount*2*(float)Math.PI;
			x = screenX + selectionRadius*(float)Math.sin(angle);
			y = screenY + selectionRadius*(float)Math.cos(angle);

			c.drawCircle(x, y, iconSize, p);
		}
		p.setStyle(old);
	}


	@Override
	public void selectObjectUnder(float screenX, float screenY)
	{
//		Sector[] visible = world.getVisibleSectors(vpX, vpY);
		for(int sec = 0; sec < visible.length; sec++)
		{
			Star[] stars = visible[sec].getStarsArray();
			for(int star = 0; star < stars.length; star++)
			{
				if(stars[star] == null) break;
				if(MathUtils.dist2Between(screenX, screenY,
						worldX2Screen(stars[star].getWorldX()),
						worldY2Screen(stars[star].getWorldY()))
						< selectionRadius * selectionRadius)
				{
					selected = stars[star];
					selectionChanged.invoke(selected);
					return;
				}
			}
		}
		if (selected != null)
		{
			selected = null;
			selectionChanged.invoke(selected);
		}
	}

	@Override
	public AbstractDisplay displayObjectUnder(float screenX, float screenY)
	{
		Sector[] visible = world.getVisibleSectors(vpX, vpY);
		for(int sec = 0; sec < visible.length; sec++)
		{
			Star[] stars = visible[sec].getStarsArray();
			for(int star = 0; star < stars.length; star++)
			{
				if(stars[star] == null) break;
				if(MathUtils.dist2Between(screenX, screenY,
						worldX2Screen(stars[star].getWorldX()),
						worldY2Screen(stars[star].getWorldY()))
						< selectionRadius * selectionRadius)
				{
					return new StarDisplay(stars[star], screenWidth, screenHeight, gameUI);
				}
			}
		}
		return null;
	}

	@Override
	public void moveViewportBy(float screenX, float screenY)
	{
		super.moveViewportBy(screenX, screenY);
		synchronized (this)
		{
			visible = world.getVisibleSectors(vpX, vpY);
		}
		setStatusText(String.format("Sector %1$s", visible[0].getName()));
	}

	@Override
	public Object getSelectedObject()
	{
		if (selected != null)
			return selected;
		else if (visible != null && visible.length > 0)
			return visible[0];
		else
			return world;
	}
}

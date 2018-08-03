package ru.sdevteam.starlit;

import android.graphics.Canvas;

import ru.sdevteam.starlit.ui.old.GameUI;
import ru.sdevteam.starlit.utils.MathUtils;
import ru.sdevteam.starlit.world.Celestial;
import ru.sdevteam.starlit.world.Planet;
import ru.sdevteam.starlit.world.Star;

/**
 * Created by user on 05.07.2016.
 */
public class StarDisplay extends AbstractDisplay
{
	private static final int INITIAL_VP_WIDTH = 2000;

	private Star star;
	private Planet selected;


	public StarDisplay(Star displaying, int displayWidth, int displayHeight, GameUI gameUI)
	{
		super(displayWidth, displayHeight, gameUI);

		defineScaleCoeff(displayWidth, INITIAL_VP_WIDTH);

		star = displaying;

		borderPaint.setStrokeWidth(selectionPaint.getStrokeWidth()/2);

		// centering view on star
		vpX = -vpWidth/2;
		vpY = -vpHeight/2;

		formStatusText(-1);
	}


	@Override
	public void drawContent(Canvas c)
	{
		c.drawColor(SPACE_COLOR);

		float x0, y0;

		// initial star coords
		x0 = worldX2Screen(0);
		y0 = worldY2Screen(0);

		Celestial[] planets = star.getOrbit();
		int[] distances = star.getOrbitDistances();

		float[] x = new float[planets.length], y = new float[planets.length];

		for(int i=0; i<planets.length; i++)
		{
			Planet planet = (Planet)planets[i];
			x[i] = worldX2Screen(planet.getX());
			y[i] = worldY2Screen(planet.getY());

			c.drawLine(x0, y0, x[i], y[i], borderPaint);
		}

		for (int i=0; i<planets.length; i++)
		{
			// orbit
			//c.drawCircle(x0, y0, distances[i]*worldToScreenScale, borderPaint);
			Planet planet = (Planet)planets[i];

			c.save();
			c.translate(x[i], y[i]);
			c.scale(worldToScreenScale, worldToScreenScale);
			c.drawCircle(0, 0, planet.getSizeType().size, planet.getMaterialType().planetPaint);
			c.restore();

			if(planet == selected)
			{
				drawSelectionAround(x[i], y[i], c, selectionPaint);
				drawResourceIcons(x[i], y[i], planet, true, c);
				drawName(x[i], y[i], planet.getName(), c);
			}
			else
			{
				drawSelectionAround(x[i], y[i], c, borderPaint);
				drawResourceIcons(x[i], y[i], planet, false, c);
			}
		}

		// drawing star
		c.save();
		c.translate(x0, y0);
		c.scale(worldToScreenScale, worldToScreenScale);
		c.drawCircle(0, 0, star.getType().size*50, star.getType().starPaint);
		c.restore();

		// drawing status text
//		drawStatusText(getStatusText(), c);
	}

	private void formStatusText(int planetIndex)
	{
		String statusText;
		if(selected != null)
		{
			statusText = String.format("Selected: %1$s (%2$dth of %3$d planets); %4$s",
					selected.getName(), planetIndex, star.getOrbit().length, selected.getTypeString());
		}
		else
		{
			statusText = String.format("Star system of %1$s, %2$d planets", star.getName(), star.getOrbit().length);
		}
		setStatusText(statusText);
	}

	@Override
	public void selectObjectUnder(float screenX, float screenY)
	{
		for(int i = 0; i<star.getOrbit().length; i++)
		{
			Planet p = (Planet)star.getOrbit()[i];
			float	sx = worldX2Screen(p.getX()),
					sy = worldY2Screen(p.getY());
			if(MathUtils.dist2Between(screenX, screenY, sx, sy) < selectionRadius*selectionRadius)
			{
				selected = p;
				selectionChanged.invoke(selected);
				formStatusText(i);
				return;
			}
		}
		if (selected != null)
		{
			selected = null;
			selectionChanged.invoke(selected);
			formStatusText(-1);
		}
	}

	@Override
	public AbstractDisplay displayObjectUnder(float screenX, float screenY)
	{
		return null;
	}

	@Override
	public void update()
	{
		super.update();
//		star.update();
	}

	@Override
	public Object getSelectedObject()
	{
		if (selected != null)
			return selected;
		else
			return star;
	}
}

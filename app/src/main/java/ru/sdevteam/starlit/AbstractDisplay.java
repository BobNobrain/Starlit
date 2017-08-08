package ru.sdevteam.starlit;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import ru.sdevteam.starlit.craft.res.ResAvailability;
import ru.sdevteam.starlit.craft.res.ResClass;
import ru.sdevteam.starlit.ui.GameUI;
import ru.sdevteam.starlit.utils.Drawing;
import ru.sdevteam.starlit.utils.MathUtils;
import ru.sdevteam.starlit.world.Celestial;

/**
 * Created by user on 26.06.2016.
 */
public abstract class AbstractDisplay
{
	protected GameUI gameUI;
	public AbstractDisplay(int screenWidth, int screenHeight, GameUI gameUI)
	{
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;

		selectionPaint = new Paint();
		selectionPaint.setColor(SELECTION_COLOR);
		selectionPaint.setStyle(Paint.Style.STROKE);
		selectionPaint.setAntiAlias(true);

		borderPaint = new Paint();
		borderPaint.setColor(BORDER_COLOR);
		borderPaint.setStyle(Paint.Style.STROKE);
		borderPaint.setAntiAlias(true);

		textPaint = new Paint();
		textPaint.setColor(TEXT_COLOR);
		textPaint.setStyle(Paint.Style.FILL);
		textPaint.setAntiAlias(true);
		textPaint.setTextAlign(Paint.Align.LEFT);
		textPaint.setTypeface(Drawing.mainFont);

		resAvailablePaint = new Paint();
		resAvailablePaint.setStyle(Paint.Style.STROKE);
		resAvailablePaint.setColor(BORDER_COLOR);
		resAvailablePaint.setAntiAlias(true);

		resAvailablePaintSelected = new Paint();
		resAvailablePaintSelected.setStyle(Paint.Style.STROKE);
		resAvailablePaintSelected.setColor(SELECTION_COLOR);
		resAvailablePaintSelected.setAntiAlias(true);

		resObtainedPaint = new Paint();
		resObtainedPaint.setStyle(Paint.Style.FILL);
		resObtainedPaint.setColor(BORDER_COLOR);
		resObtainedPaint.setAntiAlias(true);

		resObtainedPaintSelected = new Paint();
		resObtainedPaintSelected.setStyle(Paint.Style.FILL);
		resObtainedPaintSelected.setColor(SELECTION_COLOR);
		resObtainedPaintSelected.setAntiAlias(true);

//		statusText = "";
		this.gameUI = gameUI;
	}

	protected void setStatusText(String text)
	{
		gameUI.setStatusText(text);
	}

	//
	// Measurement
	//

	protected int vpX, vpY, vpWidth, vpHeight;
	protected int screenWidth, screenHeight;
	protected float screenToWorldScale; // worldPix/screenPix
	protected float worldToScreenScale; // screenPix/worldPix
	protected float selectionRadius;
	protected float iconSize; // also is 0.5% of screen width

	protected void defineScaleCoeff(float screenStandardLen, int worldStandardLen)
	{
		screenToWorldScale = worldStandardLen/screenStandardLen;
		worldToScreenScale = screenStandardLen/worldStandardLen;

		initViewportSize();
		initSizes();
	}
	protected void initViewportSize()
	{
		vpWidth = (int)(screenWidth * screenToWorldScale);
		vpHeight = (int)(screenHeight * screenToWorldScale);
	}

	protected void initSizes()
	{
		iconSize = screenWidth/200F;
		textPaint.setTextSize(TEXT_SIZE_REL*iconSize);
		selectionPaint.setStrokeWidth(iconSize);
		selectionRadius = screenWidth / 25;
		borderPaint.setStrokeWidth(iconSize/2);

		resAvailablePaint.setStrokeWidth(iconSize/2);
		resAvailablePaintSelected.setStrokeWidth(iconSize/2);

		triangleIcon = Drawing.triangleUp(3*iconSize);
		energyIcon = Drawing.energy(3*iconSize);
	}

	protected float worldX2Screen(int worldX) { return (worldX - vpX)/ screenToWorldScale; }
	protected float worldY2Screen(int worldY) { return (worldY - vpY)/ screenToWorldScale; }
	protected int screenX2World(float screenX) { return (int) (screenX * screenToWorldScale) + vpX; }
	protected int screenY2World(float screenY) { return (int) (screenY * screenToWorldScale) + vpY; }

	public void moveViewportBy(float screenX, float screenY)
	{
		int worldDX = (int)(screenX * screenToWorldScale),
			worldDY = (int)(screenY * screenToWorldScale);
		vpX += worldDX;
		vpY += worldDY;
	}

	//
	// Drawing
	//

	public static final int SELECTION_COLOR = Color.WHITE;
	public static final int SPACE_COLOR = Color.BLACK;
	public static final int BORDER_COLOR = Color.rgb(32, 32, 32);
	protected static final int TEXT_COLOR = Color.rgb(16, 96, 192);
	protected static final int TEXT_SIZE_REL = 5;


//	protected String statusText;
//	protected String getStatusText() { return statusText; }

	public abstract void drawContent(Canvas c);

	protected Paint selectionPaint, borderPaint, textPaint;
	private Paint resAvailablePaint, resAvailablePaintSelected,
				resObtainedPaint, resObtainedPaintSelected;

	private Path triangleIcon, energyIcon;

	protected void drawSelectionAround(float screenX, float screenY, Canvas c, Paint p)
	{
		c.drawCircle(screenX, screenY, selectionRadius, p);
	}

	protected void drawStatusText(String statusText, Canvas c)
	{
		c.drawText(statusText, iconSize, screenHeight-2*iconSize, textPaint);
	}

	protected void drawResourceIcons(float centerX, float centerY, Celestial body, boolean selected, Canvas c)
	{
		float	penX = centerX + selectionRadius,
				penY = centerY + selectionRadius;

		if(body.isResourceAvailableInSystem(ResClass.ORE))
		{
			c.translate(penX, penY);
			c.drawPath(triangleIcon, selected? resAvailablePaintSelected:resAvailablePaint);
			c.translate(-penX, -penY);
			penX += iconSize*4;
		}
		if(body.isResourceAvailableInSystem(ResClass.GAS))
		{
			c.drawCircle(penX, penY, 1.5F*iconSize, selected? resAvailablePaintSelected:resAvailablePaint);
			penX += iconSize*4;
		}
		if(body.isResourceAvailableInSystem(ResClass.ENERGY))
		{
			c.translate(penX, penY);
			c.drawPath(energyIcon, selected? resAvailablePaintSelected:resAvailablePaint);
			c.translate(-penX, -penY);
		}
	}

	protected void drawName(float cx, float cy, String name, Canvas c)
	{
		cx += selectionRadius; cy -= selectionRadius;
		c.drawText(name, cx, cy, textPaint);
	}

	//
	// Selecting & hierarchy
	//

	public abstract void selectObjectUnder(float screenX, float screenY);
	public abstract AbstractDisplay displayObjectUnder(float screenX, float screenY);


	public void update()
	{

	}
}

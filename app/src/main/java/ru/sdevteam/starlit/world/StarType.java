package ru.sdevteam.starlit.world;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;

/**
 * Created by user on 23.06.2016.
 */
public enum StarType
{
	RED_DWARF	(4,	Color.rgb(255,51,51),	2,	0.20F,	0.10F,	0.10F),
	WHITE_DWARF	(4,	Color.WHITE,			2,	0.20F,	0.10F,	0.10F),
	REGULAR		(5,	Color.rgb(255,238,85),	2,	0.25F,	0.05F,	0.05F),
	RED_GIANT	(7,	Color.rgb(255,51,51),	2,	0.17F,	0.10F,	0.05F),
	BLUE_GIANT	(7,	Color.rgb(136,170,255),	2,	0.13F,	0.10F,	0.05F),
	NEUTRON		(3,	Color.rgb(153,238,250),	4,	0.04F,	0.50F,	0.20F),
	BLACK_HOLE	(3,	Color.BLACK,			5,	0.01F,	0.85F,	0.20F, true)
	;

	private static final class Kostyl
	{
		static final int BLACK_HOLE_COLOR = Color.argb(76, 255, 255, 170);
	}

	private static final int HALO_SIZE_COEFF = 4;

	public int size, color, techLevel;
	public float spawnAbility, emptyProb, psaDec;
	public Paint starPaint;

	StarType(int size, int color, int techLevel, float spawnAbility, float emptyProb, float psaDec, boolean isBlackHole)
	{
		// why so? for simple using this size in drawing methods; 4 is just halo size + star actual radius
		this.size = HALO_SIZE_COEFF*size;
		this.color = color;
		this.techLevel = techLevel;
		this.spawnAbility = spawnAbility;
		this.emptyProb = emptyProb;
		this.psaDec = psaDec;

		starPaint = new Paint();
		int[] colors = { color, color, color & 0x4cFFFFFF, color & 0x00FFFFFF };
		float[] stops = { 0.0F, 1F/HALO_SIZE_COEFF, 1F/HALO_SIZE_COEFF + 0.1F, 0.95F };
		if(isBlackHole)
		{
			colors[2] = Kostyl.BLACK_HOLE_COLOR;
		}

		RadialGradient gr = new RadialGradient(0F, 0F, 4*size, colors, stops, Shader.TileMode.CLAMP);
		starPaint.setShader(gr);
	}

	StarType(int size, int color, int techLevel, float spawnAbility, float emptyProb, float psaDec)
	{
		this(size, color, techLevel, spawnAbility, emptyProb, psaDec, false);
	}
}

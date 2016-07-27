package ru.sdevteam.starlit.world;

import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by user on 04.07.2016.
 */
public enum PlanetMaterialType
{
	ROCK	(PlanetMaterialClass.SOLID,	Color.rgb( 85, 85, 85),	0.2F,	1.0F, 0.43F, 0.2F, "rock"),
	WATER	(PlanetMaterialClass.SOLID,	Color.rgb( 34,102,204),	0.1F,	0.0F, 0.27F, 0.0F, "water"),
	ICE		(PlanetMaterialClass.SOLID,	Color.rgb(123,221,255),	0.2F,	0.0F, 0.00F, 0.2F, "ice"),
	LIFE	(PlanetMaterialClass.SOLID,	Color.rgb( 34,170, 34),	0.7F,	0.0F, 0.15F, 0.0F, "biomass"),
	UL_GAS	(PlanetMaterialClass.GAS,	Color.rgb(221,119, 34),	0.0F,	0.0F, 0.05F, 0.3F, "useless gas"),
	UF_GAS	(PlanetMaterialClass.GAS,	Color.rgb( 17, 51,255),	0.0F,	0.0F, 0.05F, 0.2F, "useful gas"),
	RARE_GAS(PlanetMaterialClass.GAS,	Color.rgb(102, 34,238),	0.0F,	0.0F, 0.05F, 0.1F, "rare gas")
	;

	public PlanetMaterialClass materialClass;
	public int color;
	public float livingSpaceAmount;
	public float[] chances;

	public String name;

	public Paint planetPaint;

	PlanetMaterialType(PlanetMaterialClass mc, int color, float lsa, float chance1, float chance2, float chance3, String s)
	{
		materialClass = mc;
		this.color = color;
		livingSpaceAmount = lsa;
		chances = new float[] { chance1, chance2, chance3 };

		name = s;

		planetPaint = new Paint();
		planetPaint.setColor(color);
		planetPaint.setAntiAlias(true);
	}

	public static PlanetMaterialType getRandom(float random, float orbitDistanceNorm)
	{
		PlanetMaterialType[] all = values();
		float spawnAbility = 0F;
		for (PlanetMaterialType type : all)
		{
			float c1 = type.chances[0], c2 = type.chances[1], p=orbitDistanceNorm;
			if(orbitDistanceNorm > 0.5F)
			{
				c1 = c2;
				c2 = type.chances[2];
				p -= 0.5F;
			}
			spawnAbility = c1 + (c2 - c1)*p*2; // linear func, y(0)=c[0], y(0.5)=c[1], y(1)=c[2]

			if(random < spawnAbility) return type;
			random -= spawnAbility;
		}
		return null;
	}
}

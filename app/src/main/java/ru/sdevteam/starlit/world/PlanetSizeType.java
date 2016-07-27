package ru.sdevteam.starlit.world;

import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by user on 04.07.2016.
 */
public enum PlanetSizeType
{
	DWARF		(6,		5,	7,	PlanetMaterialClass.SOLID,	0.35F, "dwarf"),
	SEMI_DWARF	(9,		10,	10,	PlanetMaterialClass.SOLID,	0.15F, "semi-dwarf"),
	EARTH		(12,	14,	14,	PlanetMaterialClass.SOLID,	0.20F, "earth"),
	SUPER_EARTH	(15,	14,	18,	PlanetMaterialClass.SOLID,	0.30F, "super-earth"),

	SEMI_GIANT	(21,	0,	22,	PlanetMaterialClass.GAS,	0.40F, "semi-giant"),
	GIANT		(27,	0,	28,	PlanetMaterialClass.GAS,	0.60F, "giant")
	;

	public int size;
	public int maxBuildings, maxOrbitSize;
	public PlanetMaterialClass materialClass;
	private float spawnAbility;

	public String name;

	PlanetSizeType(int size, int maxBuildings, int maxOrbitSize, PlanetMaterialClass mc, float spawnAbility, String s)
	{
		this.size = size;
		this.maxBuildings = maxBuildings;
		this.maxOrbitSize = maxOrbitSize;
		this.materialClass = mc;
		this.spawnAbility = spawnAbility;
		name = s;
	}

	public static PlanetSizeType getRandomFor(float random, PlanetMaterialType pmt)
	{
		PlanetSizeType[] all = values();
		for (PlanetSizeType type : all)
		{
			if(type.materialClass != pmt.materialClass) continue;
			if(random < type.spawnAbility) return type;
			random -= type.spawnAbility;
		}
		return null;
	}
}

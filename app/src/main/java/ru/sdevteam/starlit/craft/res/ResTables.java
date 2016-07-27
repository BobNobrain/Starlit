package ru.sdevteam.starlit.craft.res;

import ru.sdevteam.starlit.world.PlanetMaterialType;

/**
 * Created by user on 11.07.2016.
 */
public class ResTables
{
	private static float[][] resAtPlanetsAbilities = {
		//	  METAL,	CRYST,	PLUTO,	FUEL,	RAREG,	FOOD
			{ 0.80F,	0.80F,	0.20F,	0.00F,	0.00F,	0.00F },	// ROCK
			{ 0.75F,	0.80F,	0.15F,	0.00F,	0.00F,	0.20F },	// WATER
			{ 0.60F,	0.60F,	0.30F,	0.00F,	0.00F,	0.00F },	// ICE
			{ 0.60F,	0.60F,	0.20F,	0.00F,	0.00F,	0.00F },	// LIFE
			// under this, table values aren't really used
			{ 0.00F,	0.00F,	0.00F,	0.00F,	0.00F,	0.00F },	// UL_GAS
			{ 0.00F,	0.00F,	0.00F,	1.00F,	0.00F,	0.00F },	// UF_GAS
			{ 0.00F,	0.00F,	0.00F,	0.70F,	1.00F,	0.00F } 	// RARE_GAS
	};
	public static float getResourceSpawnAbilityAt(ResType rt, PlanetMaterialType pmt)
	{
		if(rt == ResType.ENERGY) return 0F;

		if(pmt == PlanetMaterialType.UL_GAS) return 0F;
		if(pmt == PlanetMaterialType.UF_GAS)
		{
			if(rt == ResType.FUEL) return 1F;
			return 0F;
		}
		if(pmt == PlanetMaterialType.RARE_GAS)
		{
			if(rt == ResType.FUEL) return 0.7F;
			if(rt == ResType.RARE_GAS) return 1F;
			return 0F;
		}

		return resAtPlanetsAbilities[pmt.ordinal()][rt.ordinal()];
	}

	private static int[][] resAtPlanetsTechLevels = {
		//	  METL,CR, PL,FUEL,RG,FOOD
			{  1,	1,	1,	0,	0,	0 },	// ROCK
			{  1,	1,	1,	0,	0,	0 },	// WATER
			{  2,	1,	2,	0,	0,	0 },	// ICE
			{  1,	1,	1,	0,	0,	0 },	// LIFE
			{  0,	0,	0,	0,	0,	0 },	// UL_GAS
			{  0,	0,	0,	3,	0,	0 },	// UF_GAS
			{  0,	0,	0,	0,	3,	0 } 	// RARE_GAS
	};
	public static int getResourceTechLevelAt(ResType rt, PlanetMaterialType pmt)
	{
		if(rt == ResType.ENERGY) return 0;
		return resAtPlanetsTechLevels[rt.ordinal()][pmt.ordinal()];
	}
}

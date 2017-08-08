package ru.sdevteam.starlit.world;

import ru.sdevteam.starlit.craft.buildings.BuildingPlace;
import ru.sdevteam.starlit.craft.buildings.BuildingSystem;
import ru.sdevteam.starlit.craft.buildings.PlanetBuildingSystem;
import ru.sdevteam.starlit.craft.res.ResClass;
import ru.sdevteam.starlit.craft.res.ResTables;
import ru.sdevteam.starlit.craft.res.ResType;

import java.util.Random;

/**
 * Created by user on 04.07.2016.
 */
public class Planet extends Celestial
{
	private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRTUVWXYZ";

	private PlanetSizeType sizeType;
	public PlanetSizeType getSizeType() { return sizeType; }
	private PlanetMaterialType materialType;
	public PlanetMaterialType getMaterialType() { return materialType; }

	public String getTypeString()
	{
		return String.format("Size: %1$s; Dominant material: %2$s", sizeType.name, materialType.name);
	}

	public Planet(Sector location, int sx, int sy, Star sun, int orbitIndex)
	{
		super(location, sx, sy, sun.getName() + "-" + ALPHABET.charAt(orbitIndex));
	}

	public void setMaterials(float random1, float random2, float orbitDistanceNorm)
	{
		materialType = PlanetMaterialType.getRandom(random1, orbitDistanceNorm);
		sizeType = PlanetSizeType.getRandomFor(random2, materialType);
		buildings = new PlanetBuildingSystem(sizeType.maxBuildings, sizeType.maxOrbitSize);
	}

	public void spawnResources(Random rnd)
	{
		ResType[] rts = ResType.values();
		for (int i = 0; i < rts.length; i++)
		{
			float ability = ResTables.getResourceSpawnAbilityAt(rts[i], materialType);
			if(rnd.nextFloat() < ability)
			{
				enableResource(rts[i]);
			}
		}
	}
}

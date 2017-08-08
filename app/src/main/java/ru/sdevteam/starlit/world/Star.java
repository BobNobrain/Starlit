package ru.sdevteam.starlit.world;

import ru.sdevteam.starlit.craft.buildings.BuildingSystem;
import ru.sdevteam.starlit.craft.buildings.PlanetBuildingSystem;
import ru.sdevteam.starlit.craft.res.ResType;

/**
 * A single star
 */
public class Star extends Celestial
{
	public static final int MAX_ORBIT_DISTANCE = 3000;
	public static final int MIN_ORBIT_DISTANCE = 50;
	public static final int MIN_ORBIT_MARGIN = 150;

	@Override
	protected float getOrbitSpeedCoeff() { return 1F; }

	private static int totalStarsCount = 0;

	private StarType type;
	public StarType getType() { return type; }

	private boolean doubled;
	// TODO: doubled stars
	public boolean isDoubled() { return doubled; }

	public Star(Sector location, int sx, int sy, StarType type, boolean doubled, String name)
	{
		super(location, sx, sy, name);
		this.type = type;
		this.doubled = doubled;
		++totalStarsCount;

		// TODO: not type.size but some actual values!
		buildings = new PlanetBuildingSystem(0, type.size);

		enableResource(ResType.ENERGY);
	}
	public Star(Sector location, int sx, int sy, StarType type, boolean doubled)
	{
		this(location, sx, sy, type, doubled, location.getName()+"-#"+(totalStarsCount+1));
	}
}

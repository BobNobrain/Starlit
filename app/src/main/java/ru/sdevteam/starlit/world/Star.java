package ru.sdevteam.starlit.world;

import ru.sdevteam.starlit.craft.res.ResType;

/**
 * Created by user on 23.06.2016.
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

		enableResource(ResType.ENERGY);
	}
	public Star(Sector location, int sx, int sy, StarType type, boolean doubled)
	{
		this(location, sx, sy, type, doubled, location.getName()+"-#"+(totalStarsCount+1));
	}

}

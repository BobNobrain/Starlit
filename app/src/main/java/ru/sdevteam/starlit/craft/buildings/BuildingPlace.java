package ru.sdevteam.starlit.craft.buildings;

import java.util.EnumSet;

/**
 * Created by user on 11.07.2016.
 */
public enum BuildingPlace
{
	GROUND, PLANET_ORBIT, STAR_ORBIT;

	public static final EnumSet<BuildingPlace> ONLY_GROUND = EnumSet.of(GROUND);
	public static final EnumSet<BuildingPlace> ONLY_PLANET_ORBIT = EnumSet.of(PLANET_ORBIT);
	public static final EnumSet<BuildingPlace> ONLY_STAR_ORBIT = EnumSet.of(STAR_ORBIT);
	public static final EnumSet<BuildingPlace> ORBIT = EnumSet.of(PLANET_ORBIT, STAR_ORBIT);
	public static final EnumSet<BuildingPlace> EVERYWHERE = EnumSet.allOf(BuildingPlace.class);
}

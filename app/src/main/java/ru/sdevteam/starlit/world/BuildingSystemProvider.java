package ru.sdevteam.starlit.world;

import ru.sdevteam.starlit.craft.buildings.PlanetBuildingSystem;

/**
 * Interface should be implemented by all classes that can provide a BuildSystem
 */
public interface BuildingSystemProvider
{
	PlanetBuildingSystem getBuildingSystem();
}

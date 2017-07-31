package ru.sdevteam.starlit.craft.buildings;

import ru.sdevteam.starlit.craft.res.ResAmount;
import ru.sdevteam.starlit.craft.res.StorageLimit;

/**
 * Compound build system (includes ground and orbit
 */
public class PlanetBuildingSystem extends BuildingSystem
{
	private BuildingSystem ground, orbit;
	public BuildingSystem getGround() { return ground; }
	public BuildingSystem getOrbit() { return orbit; }

	public PlanetBuildingSystem(int groundFreeSpace, int orbitFreeSpace)
	{
		super(0, null);

		ground = new BuildingSystem(groundFreeSpace, BuildingPlace.GROUND);
		orbit = new BuildingSystem(orbitFreeSpace, BuildingPlace.PLANET_ORBIT);
	}

	@Override
	public int getFreeSpace()
	{
		return ground.getFreeSpace() + orbit.getFreeSpace();
	}

	@Override
	public int getTotalSpace()
	{
		return ground.getTotalSpace() + orbit.getTotalSpace();
	}

	@Override
	public BuildFailedReason build(Building b)
	{
		// this should never be called
		return BuildFailedReason.INVALID_LOCATION;
	}

	@Override
	public ResAmount getTotalResourcesStored()
	{
		ResAmount result = new ResAmount();
		result.increaseBy(ground.getTotalResourcesStored());
		result.increaseBy(orbit.getTotalResourcesStored());
		return result;
	}

	@Override
	public StorageLimit getStorageLimit()
	{
		StorageLimit r = new StorageLimit();
		r.increaseBy(ground.getStorageLimit());
		r.increaseBy(orbit.getStorageLimit());
		return super.getStorageLimit();
	}

	@Override
	public ResAmount storeAsMuchAsPossible(ResAmount resources)
	{
		ResAmount rest = ground.storeAsMuchAsPossible(resources);
		return orbit.storeAsMuchAsPossible(rest);
	}

	@Override
	public boolean withdraw(ResAmount resources)
	{
		ResAmount r = getTotalResourcesStored();
		if (r.isGreaterOrEqualThan(resources))
		{
			ResAmount res = ground.withdrawAsMuchAsPossible(resources);
			orbit.withdraw(res);
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public ResAmount withdrawAsMuchAsPossible(ResAmount resources)
	{
		ResAmount r = ground.withdrawAsMuchAsPossible(resources);
		return orbit.withdrawAsMuchAsPossible(r);
	}

	@Override
	public void update(long worldTime)
	{
		ground.update(worldTime);
		orbit.update(worldTime);
	}
}

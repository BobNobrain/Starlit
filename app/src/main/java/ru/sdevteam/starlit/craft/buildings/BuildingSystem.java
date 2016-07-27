package ru.sdevteam.starlit.craft.buildings;

import ru.sdevteam.starlit.craft.res.ResAmount;

/**
 * Created by user on 11.07.2016.
 */
public class BuildingSystem
{
	private static final int NO_FREE_SPACE = -1;

	private Building[] buildings;
	public int getTotalSpace() { return buildings.length; }
	public int getFreeSpace()
	{
		int result = 0;
		for (int i = 0; i < buildings.length; i++)
		{
			if(buildings[i] == null) ++result;
		}
		return result;
	}

	private BuildingPlace location;


	public BuildingSystem(int freeSpace, BuildingPlace location)
	{
		this.location = location;
		buildings = new Building[freeSpace];
	}


	public BuildFailedReason build(Building b)
	{
		if(!b.canBePlacedAt(location)) return BuildFailedReason.INVALID_LOCATION;
		if(!getTotalResourcesStored().isGreaterOrEqualThan(b.getPrice())) return BuildFailedReason.NOT_ENOUGH_RESOURCES;

		int index = getFreeIndex();
		if(index == NO_FREE_SPACE) return BuildFailedReason.NOT_ENOUGH_PLACE;

		buildings[index] = b;
		b.appendToSystem(this);

		return BuildFailedReason.NONE;
	}

	private int getFreeIndex()
	{
		for (int i = 0; i < buildings.length; i++)
		{
			if(buildings[i] == null) return i;
		}
		return NO_FREE_SPACE;
	}

	public ResAmount getTotalResourcesStored()
	{
		// TODO:
		return new ResAmount(10);
	}

	private long lastTimeUpdated;
	public void update(long worldTime)
	{
		long dt = worldTime - lastTimeUpdated;
		int seconds = (int)(dt/1000);

		for(int i = 0; i < buildings.length; i++)
		{
			if(buildings[i] != null)
			{
				buildings[i].update(worldTime, seconds);
			}
		}

		lastTimeUpdated = worldTime;
	}


	public enum BuildFailedReason
	{
		NONE("Build succeed!"),
		INVALID_LOCATION("This can't be built there!"),
		NOT_ENOUGH_RESOURCES("You need more resources for that!"),
		NOT_ENOUGH_PLACE("There is no more place for building!")
		;
		public String message;

		BuildFailedReason(String why)
		{
			message = why;
		}
	}
}

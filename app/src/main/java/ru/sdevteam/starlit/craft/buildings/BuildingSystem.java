package ru.sdevteam.starlit.craft.buildings;

import ru.sdevteam.starlit.craft.res.ResAmount;
import ru.sdevteam.starlit.craft.res.StorageLimit;

/**
 * Class represents place where buildings can be built
 */
public class BuildingSystem implements IStorage
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


	public BuildFailedReason canBuild(Building b)
	{
		if(!b.canBePlacedAt(location)) return BuildFailedReason.INVALID_LOCATION;
		if(!getTotalResourcesStored().isGreaterOrEqualThan(b.getPrice())) return BuildFailedReason.NOT_ENOUGH_RESOURCES;

		int index = getFreeIndex();
		if(index == NO_FREE_SPACE) return BuildFailedReason.NOT_ENOUGH_PLACE;

		return BuildFailedReason.NONE;
	}

	public void build(Building b)
	{
		if (canBuild(b) == BuildFailedReason.NONE)
		{
			withdraw(b.price);
			forceBuild(b);
		}
	}

	public void forceBuild(Building b) throws RuntimeException
	{
		int index = getFreeIndex();
		if (index == NO_FREE_SPACE)
			throw new RuntimeException("Cannot force build when there is no space!");
		buildings[index] = b;
		b.appendToSystem(this);
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
		ResAmount stored = new ResAmount();
		for (Building b: buildings)
		{
			if (b instanceof IStorage)
			{
				stored.increaseBy(((IStorage) b).getTotalResourcesStored());
			}
		}
		return stored;
	}

	/**
	 * Total limit of storages within system
	 * @return total limit
	 */
	public StorageLimit getStorageLimit()
	{
		StorageLimit limit = new StorageLimit();
		for (Building b: buildings)
		{
			if (b instanceof IStorage)
			{
				limit.increaseBy(((IStorage) b).getStorageLimit());
			}
		}
		return limit;
	}

	/**
	 * Method tries to store given resources in all storages within the build system and
	 * stores as much of them as possible
	 * @param resources ResAmount to store
	 * @return ResAmount for which there were no place to store (or zero values if everything fitted)
	 */
	public ResAmount storeAsMuchAsPossible(ResAmount resources)
	{
		for (Building b: buildings)
		{
			if (b instanceof IStorage)
			{
				IStorage s = (IStorage) b;
				resources = s.storeAsMuchAsPossible(resources);
				if (resources.isEmpty()) break;
			}
		}
		return resources;
	}

	/**
	 * Method tries to withdraw resources from any storage within system. If there are no enough
	 * resources, it doesn't do anything and returns false; otherwise returns true
	 * @param resources how much to withdraw
	 * @return was the operation successful
	 */
	public boolean withdraw(ResAmount resources)
	{
		ResAmount total = getTotalResourcesStored();
		if (total.isGreaterOrEqualThan(resources))
		{
			withdrawAsMuchAsPossible(resources);
			return true;
		}
		return false;
	}

	/**
	 * Method tries to withdraw resources from any storage within system. If there are no enough resources,
	 * it withdraws all it can.
	 * @param resources how much to withdraw
	 * @return how much resources were withdrawn
	 */
	public ResAmount withdrawAsMuchAsPossible(ResAmount resources)
	{
		ResAmount withdrawnTotal = new ResAmount();
		for (Building b: buildings)
		{
			if (b instanceof IStorage)
			{
				IStorage s = (IStorage) b;
				ResAmount withdrawnFromS = s.withdrawAsMuchAsPossible(resources);
				withdrawnTotal.increaseBy(withdrawnFromS);
				resources.decreaseBy(withdrawnFromS);
				if (resources.isEmpty()) break;
			}
		}
		return resources;
	}

	private int lastTimeUpdatedSeconds = 0; // TODO: save this field when serializing!
	public void update(long worldTime)
	{
		int seconds = (int)(worldTime/1000);

		for(int i = 0; i < buildings.length; i++)
		{
			if(buildings[i] != null)
			{
				buildings[i].update(worldTime, seconds - lastTimeUpdatedSeconds);
			}
		}
		lastTimeUpdatedSeconds = seconds;
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

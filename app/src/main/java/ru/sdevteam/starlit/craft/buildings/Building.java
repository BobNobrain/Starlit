package ru.sdevteam.starlit.craft.buildings;

import ru.sdevteam.starlit.craft.res.ResAmount;
import ru.sdevteam.starlit.craft.res.ResType;
import ru.sdevteam.starlit.craft.res.StorageLimit;

import java.util.EnumSet;

/**
 * Created by user on 11.07.2016.
 */
public class Building
{
	protected String name;

	protected BuildingSystem system;
	void appendToSystem(BuildingSystem sys) { system = sys; }

	protected int timeToBuild;
	public int getTimeToBuild() { return timeToBuild; }
	protected int populationNeeded;
	public int getPopulationNeeded() { return populationNeeded; }

	protected ResAmount price;
	public ResAmount getPrice() { return price; }

	protected ResAmount destructionProfit;
	public ResAmount getDestructionProfit() { return destructionProfit; }

	protected EnumSet<BuildingPlace> placeableAt;
	public boolean canBePlacedAt(BuildingPlace place) { return placeableAt.contains(place); }
	public EnumSet<BuildingPlace> getPlaces() { return placeableAt; }


	protected Building(String name, ResAmount price, ResAmount destructionCost,
					   int poplNeeded, int timeNeeded, EnumSet<BuildingPlace> placeableAt)
	{
		this.name = name;
		timeToBuild = timeNeeded;
		populationNeeded = poplNeeded;
		this.price = price;
		destructionProfit = destructionCost;
		this.placeableAt = placeableAt;

		stored = null;// new ResAmount();
		limit = null;// new StorageLimit();
	}

	public Building(Building sample)
	{
		this.name = sample.name;
		timeToBuild = sample.timeToBuild;
		populationNeeded = sample.populationNeeded;
		this.price = sample.price;
		destructionProfit = sample.destructionProfit;
		this.placeableAt = sample.placeableAt;

		stored = sample.stored;
		limit = sample.limit;
	}


	public void update(long worldTime, int fullSecondsElapsed)
	{

	}

	//
	// storage api
	//
	protected StorageLimit limit;
	protected ResAmount stored;

	public boolean isPublicStorage() { return false; }

	public StorageLimit getStorageLimit()
	{
		return limit;
	}

	public int getStoredAmount(ResType rt)
	{
		return stored.getAmountOf(rt);
	}

	public boolean hasStoredAmount(ResType rt, int howMuch)
	{
		return stored.getAmountOf(rt) >= howMuch;
	}

	public boolean addStoredAmount(ResType rt, int addition)
	{
		int newAmount = stored.getAmountOf(rt.resClass) + addition;
		if(newAmount <= limit.getLimitFor(rt.resClass))
		{
			stored.setAmountOf(rt, stored.getAmountOf(rt) + addition);
			return true;
		}
		return false;
	}

	public boolean addStoredAmount(ResAmount res)
	{
		if(!limit.canStore(stored, res)) return false;
		stored.increaseBy(res);
		return true;
	}

	public boolean withdrawResources(ResAmount res)
	{
		return stored.decreaseBy(res);
	}

	public ResAmount withdrawAsMuchAsPossible(ResAmount maxToWithdraw)
	{
		if(stored.decreaseBy(maxToWithdraw))
		{
			return maxToWithdraw;
		}

		ResAmount result = new ResAmount();

		ResType[] types = ResType.values();
		for (ResType type : types)
		{
			int has = stored.getAmountOf(type);
			int needed = maxToWithdraw.getAmountOf(type);
			if(has >= needed)
			{
				stored.setAmountOf(type, has - needed);
				result.setAmountOf(type, needed);
			}
			else
			{
				stored.setAmountOf(type, 0);
				result.setAmountOf(type, has);
			}
		}

		return result;
	}


	public enum Type
	{
		// every building can be a storage!
		// some are public storages, and some are not
		PLANT, STATE, SCIENCE, SHIPYARD, STORAGE, WARFARE, OTHER
	}
}

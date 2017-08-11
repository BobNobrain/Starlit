package ru.sdevteam.starlit.craft.buildings;

import org.json.JSONException;
import org.json.JSONObject;
import ru.sdevteam.starlit.craft.res.ResAmount;
import ru.sdevteam.starlit.craft.res.ResType;
import ru.sdevteam.starlit.craft.res.StorageLimit;

import java.util.EnumSet;

/**
 * Class represents Storage building that can store resources
 */
public class Storage extends Building implements IStorage
{
	protected StorageLimit limit;
	protected ResAmount stored;

	protected Storage(String name, ResAmount price, ResAmount destructionCost,
					  int poplNeeded, int timeNeeded, EnumSet<BuildingPlace> placeableAt,
					  StorageLimit limit)
	{
		super(name, price, destructionCost, poplNeeded, timeNeeded, placeableAt);
		this.limit = limit;
		this.stored = new ResAmount();
	}

	public Storage(Building sample)
	{
		super(sample);
	}

	public Storage(JSONObject params) throws JSONException
	{
		super(params);
		this.limit = StorageLimit.fromJSON(params.getJSONArray("storage_limit"));
		this.stored = new ResAmount();

		if (description == null)
		{
			description = "Can store resources produced at the planet. Maximally stores \n" + limit.toString();
		}
	}

	@Override
	public Type getType()
	{
		return Type.STORAGE;
	}

	@Override
	public ResAmount getTotalResourcesStored()
	{
		return stored;
	}

	@Override
	public StorageLimit getStorageLimit()
	{
		return limit;
	}

	@Override
	public ResAmount storeAsMuchAsPossible(ResAmount amount)
	{
		ResAmount remainder = new ResAmount();
		StorageLimit storedAsLimit = new StorageLimit(stored);
		for (ResType rt: ResType.values())
		{
			int free = limit.getLimitFor(rt.resClass) - storedAsLimit.getLimitFor(rt.resClass);
			int given = amount.getAmountOf(rt);
			if (given > free)
			{
				stored.addAmountOf(rt, free);
				remainder.setAmountOf(rt, given - free);
			}
			else
			{
				stored.addAmountOf(rt, given);
			}
		}
		return remainder;
	}

	@Override
	public boolean withdraw(ResAmount amount)
	{
		if (stored.isGreaterOrEqualThan(amount))
		{
			stored.decreaseBy(amount);
			return true;
		}
		return false;
	}

	@Override
	public ResAmount withdrawAsMuchAsPossible(ResAmount amount)
	{
		ResAmount withdrawn = new ResAmount();
		for (ResType rt: ResType.values())
		{
			int asked = amount.getAmountOf(rt);
			int has = stored.getAmountOf(rt);
			if (asked > has)
			{
				stored.setAmountOf(rt, 0);
				withdrawn.setAmountOf(rt, has);
			}
			else
			{
				stored.addAmountOf(rt, -asked);
				withdrawn.setAmountOf(rt, asked);
			}
		}
		return withdrawn;
	}
}

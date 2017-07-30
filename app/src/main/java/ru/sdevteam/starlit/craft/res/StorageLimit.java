package ru.sdevteam.starlit.craft.res;

import org.json.JSONArray;
import org.json.JSONException;
import ru.sdevteam.starlit.craft.buildings.Storage;

/**
 * Created by user on 11.07.2016.
 */
public class StorageLimit
{
	private int[] limits;

	public StorageLimit()
	{
		limits = new int[ResClass.values().length];
		for (int i = 0; i < limits.length; i++)
		{
			limits[i] = 0;
		}
	}

	public StorageLimit(ResAmount toFit)
	{
		this();
		for (int i = 0; i < limits.length; i++)
		{
			limits[i] += toFit.getAmountOf(ResClass.values()[i]);
		}
	}

	public StorageLimit(int ...lims)
	{
		this();
		for (int i = 0; i < limits.length && i < lims.length; i++)
		{
			limits[i] = lims[i];
		}
	}

	public int getLimitFor(ResClass cl)
	{
		return limits[cl.ordinal()];
	}
	public void setLimitFor(ResClass cl, int limit)
	{
		limits[cl.ordinal()] = limit;
	}

	public boolean canStore(ResAmount res)
	{
		// total amounts of resources of given class
		int[] amounts = new int[limits.length];

		ResType[] types = ResType.values();
		for (int i = 0; i < types.length; i++)
		{
			amounts[types[i].resClass.ordinal()] += res.getAmountOf(types[i]);
		}

		// checking amounts to be in appropriate limits
		for (int i = 0; i < amounts.length; i++)
		{
			if(amounts[i] > limits[i]) return false;
		}
		return true;
	}

	public boolean canStore(ResAmount base, ResAmount addition)
	{
		int[] amounts = new int[limits.length];

		ResType[] types = ResType.values();
		for (int i = 0; i < types.length; i++)
		{
			amounts[types[i].resClass.ordinal()] += base.getAmountOf(types[i]) + addition.getAmountOf(types[i]);
		}

		// checking amounts to be in appropriate limits
		for (int i = 0; i < amounts.length; i++)
		{
			if(amounts[i] > limits[i]) return false;
		}
		return true;
	}

	public static StorageLimit fromJSON(JSONArray arr) throws JSONException
	{
		StorageLimit result = new StorageLimit();
		for (int i = 0; i < result.limits.length; i++)
		{
			result.limits[i] = arr.getInt(i);
		}
		return result;
	}
}

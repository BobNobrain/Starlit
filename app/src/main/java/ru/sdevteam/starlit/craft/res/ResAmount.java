package ru.sdevteam.starlit.craft.res;

import org.json.JSONArray;
import org.json.JSONException;

/**
 * Created by user on 11.07.2016.
 */
public class ResAmount
{
	public static final ResAmount ENERGY_UNIT = new ResAmount(0, 0, 0, 0, 0, 0, 1);

	private int[] amounts;

	public ResAmount(int allValue)
	{
		amounts = new int[ResType.values().length];
		for (int i = 0; i < amounts.length; i++)
		{
			amounts[i] = allValue;
		}
	}
	public ResAmount()
	{
		this(0);
	}
	public ResAmount(int ...values)
	{
		this(0);
		for (int i = 0; i < values.length && i<amounts.length; i++)
		{
			amounts[i] = values[i];
		}
	}

	public int getAmountOf(ResType rt)
	{
		return amounts[rt.ordinal()];
	}

	public int getAmountOf(ResClass rc)
	{
		ResType[] types = ResType.values();
		int result = 0;
		for (int i = 0; i < types.length; i++)
		{
			if(types[i].resClass == rc) result += amounts[i];
		}
		return result;
	}

	public void setAmountOf(ResType rt, int amount)
	{
		amounts[rt.ordinal()] = amount;
	}

	public void increaseBy(ResAmount res)
	{
		for (int i = 0; i < amounts.length; i++)
		{
			amounts[i] += res.amounts[i];
		}
	}
	public boolean decreaseBy(ResAmount res)
	{
		if(!isGreaterOrEqualThan(res)) return false;

		for (int i = 0; i < amounts.length; i++)
		{
			amounts[i] += res.amounts[i];
		}
		return true;
	}

	public ResAmount multiply(int m)
	{
		ResAmount result = new ResAmount();
		for (int i = 0; i < amounts.length; i++)
			result.amounts[i] = amounts[i] * m;
		return result;
	}

	public boolean isGreaterOrEqualThan(ResAmount res)
	{
		for (int i = 0; i < amounts.length; i++)
		{
			if(amounts[i] < res.amounts[i]) return false;
		}
		return true;
	}

	public static ResAmount fromJSON(JSONArray values) throws JSONException
	{
		ResAmount result = new ResAmount();
		for (int i = 0; i < result.amounts.length; i++)
		{
			result.amounts[i] = values.getInt(i);
		}
		return result;
	}
}

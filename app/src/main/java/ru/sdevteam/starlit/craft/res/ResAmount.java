package ru.sdevteam.starlit.craft.res;

/**
 * Created by user on 11.07.2016.
 */
public class ResAmount
{
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

	public boolean isGreaterOrEqualThan(ResAmount res)
	{
		for (int i = 0; i < amounts.length; i++)
		{
			if(amounts[i] < res.amounts[i]) return false;
		}
		return true;
	}
}

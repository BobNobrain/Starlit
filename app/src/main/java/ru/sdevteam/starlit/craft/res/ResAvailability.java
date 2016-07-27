package ru.sdevteam.starlit.craft.res;

/**
 * Created by user on 11.07.2016.
 */
public class ResAvailability
{
	private boolean[] available;

	public ResAvailability()
	{
		available = new boolean[ResType.values().length];
		for (int i = 0; i < available.length; i++)
		{
			available[i] = false;
		}
	}

	public void enable(ResType rt)
	{
		available[rt.ordinal()] = true;
	}
	public void disable(ResType rt)
	{
		available[rt.ordinal()] = false;
	}

	public boolean isAvailable(ResType rt)
	{
		return available[rt.ordinal()];
	}

	public boolean isAvailable(ResClass rc)
	{
		ResType[] ts = ResType.values();
		for (int i = 0; i < ts.length; i++)
		{
			if(ts[i].resClass == rc && available[i]) return true;
		}
		return false;
	}
}

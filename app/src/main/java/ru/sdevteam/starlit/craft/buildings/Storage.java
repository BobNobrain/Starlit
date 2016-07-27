package ru.sdevteam.starlit.craft.buildings;

import ru.sdevteam.starlit.craft.res.ResAmount;
import ru.sdevteam.starlit.craft.res.ResType;
import ru.sdevteam.starlit.craft.res.StorageLimit;

import java.util.EnumSet;

/**
 * Created by user on 11.07.2016.
 */
public class Storage extends Building
{

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

	@Override
	public final boolean isPublicStorage() { return true; }
}

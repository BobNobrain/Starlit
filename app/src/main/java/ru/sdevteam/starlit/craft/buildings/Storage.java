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
public class Storage extends Building
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
}

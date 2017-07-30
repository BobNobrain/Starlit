package ru.sdevteam.starlit.craft.buildings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ru.sdevteam.starlit.craft.res.ResAmount;
import ru.sdevteam.starlit.craft.res.StorageLimit;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.EnumSet;

/**
 * Class is used to load and store all building samples
 */
public class BuildingsRegistry
{
	private static JSONArray registry;

	public static void registerFromJSON(JSONArray bs) throws JSONException, ReflectiveOperationException
	{
		registry = bs;
//		for (int i = 0; i < bs.length(); i++)
//		{
//			JSONObject b = bs.getJSONObject(i);
//		}
	}
	private static Building createFromJSONData(JSONObject data)
	{
		try
		{
			Class bc = Class.forName("ru.sdevteam." + data.getString("class"));
			if (Building.class.isAssignableFrom(bc))
			{
				Constructor c = bc.getConstructor(JSONObject.class);
				Building result = (Building) c.newInstance(data);
				return result;
			}
			else
			{
				throw new ClassCastException("Given class " + data.getString("class") + " is not a subclass of Building");
			}
		}
		catch (Exception ex)
		{
			System.err.println("Registry: get building error: " + ex.getMessage());
			return null;
		}
	}
	public static Building forName(String name)
	{
		for (int i = 0; i < registry.length(); i++)
		{
			try
			{
				JSONObject b = registry.getJSONObject(i);
				if (b.getString("name").equals(name))
					return createFromJSONData(b);
			}
			catch (Exception ex)
			{
				return null;
			}
		}
		return null;
	}

	private static class InfoContainer
	{
		String name;
		ResAmount price;
		StorageLimit storage;
		int populationNeeded, timeToBuild;
		ResAmount destructionProfit;
		EnumSet<BuildingPlace> placeableAt;
	}

	private static abstract class BInstance
	{
		Building sample;
		public abstract Building getInstance();
		BInstance(Building theSample) { sample = theSample; }
	}
	private static class BStorageInstance extends BInstance
	{
		BStorageInstance(Building theSample) { super(theSample); }
		@Override public Building getInstance() { return new Storage(sample); }
	}
	private static class BPlantInstance extends BInstance
	{
		BPlantInstance(Building theSample) { super(theSample); }
		@Override public Building getInstance() { return new Storage(sample); } // TODO
	}

	public static final BStorageInstance GOVERNMENT = new BStorageInstance(new Storage(
			"Government",
			new ResAmount(5, 0, 0, 0, 0, 0, 0),
			new ResAmount(4, 0, 0, 0, 0, 0, 0),
			1, 1000,
			BuildingPlace.ONLY_GROUND,
			new StorageLimit(10, 7, 10, 15)
	));

	public static final BStorageInstance STORAGE = new BStorageInstance(new Storage(
			"Storage",
			new ResAmount(8, 0, 0, 0, 0, 0, 0),
			new ResAmount(6, 0, 0, 0, 0, 0, 0),
			0, 1500,
			BuildingPlace.EVERYWHERE,
			new StorageLimit(25, 15, 20, 40)
	));

	public static final BStorageInstance WAREHOUSE = new BStorageInstance(new Storage(
			"Warehouse",
			new ResAmount(15, 3, 0, 0, 0, 0, 5),
			new ResAmount(12, 2, 0, 0, 0, 0, 0),
			10, 5000,
			BuildingPlace.EVERYWHERE,
			new StorageLimit(50, 40, 50, 100)
	));

	public static final BStorageInstance MASSIVE_WAREHOUSE = new BStorageInstance(new Storage(
			"Massive warehouse",
			new ResAmount(35, 10, 0, 0, 0, 0, 10),
			new ResAmount(30, 6, 0, 0, 0, 0, 0),
			10, 5000,
			BuildingPlace.EVERYWHERE,
			new StorageLimit(125, 65, 150, 200)
	));
}

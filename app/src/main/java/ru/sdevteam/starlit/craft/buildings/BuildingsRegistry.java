package ru.sdevteam.starlit.craft.buildings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ru.sdevteam.starlit.craft.res.ResAmount;
import ru.sdevteam.starlit.craft.res.StorageLimit;
import ru.sdevteam.starlit.world.PlanetMaterialType;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Vector;

/**
 * Class is used to load and store all building samples
 */
public class BuildingsRegistry
{
	private static JSONArray registry;
	private static Vector<Building> samples;

	public static void registerFromJSON(JSONArray bs) throws JSONException, ReflectiveOperationException
	{
		registry = bs;
		samples = new Vector<>(registry.length());
		for (int i = 0; i < bs.length(); i++)
		{
			JSONObject b = bs.getJSONObject(i);
			samples.add(createFromJSONData(b));
		}
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
}

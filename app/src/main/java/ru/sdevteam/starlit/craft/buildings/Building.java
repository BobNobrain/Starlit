package ru.sdevteam.starlit.craft.buildings;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ru.sdevteam.starlit.craft.res.ResAmount;
import ru.sdevteam.starlit.world.Celestial;

import java.util.EnumSet;

/**
 * Class represents a basic building with all its parameters
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
	}

	public Building(Building sample)
	{
		this.name = sample.name;
		timeToBuild = sample.timeToBuild;
		populationNeeded = sample.populationNeeded;
		this.price = sample.price;
		destructionProfit = sample.destructionProfit;
		this.placeableAt = sample.placeableAt;
	}

	public Building(JSONObject params) throws JSONException
	{
		name = params.getString("name");
		timeToBuild = params.getInt("time");
		populationNeeded = params.getInt("population");
		price = ResAmount.fromJSON(params.getJSONArray("price"));
		destructionProfit = ResAmount.fromJSON(params.getJSONArray("destruct_price"));

		JSONArray places = params.getJSONArray("place");
		EnumSet<BuildingPlace> ps = EnumSet.noneOf(BuildingPlace.class);
		for (int i = 0; i < places.length(); i++)
		{
			String name = places.getString(i);
			ps.add(BuildingPlace.valueOf(name));
		}
		placeableAt = ps;
	}


	public void update(long worldTime, int fullSecondsElapsed)
	{

	}


	public boolean canBePlacedAt(Celestial c)
	{
		return true;
	}

	public Type getType() { return Type.OTHER; }

	public enum Type
	{
		STATE, STORAGE, PLANT, SCIENCE, SHIPYARD, WARFARE, OTHER
	}
}

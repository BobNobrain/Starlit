package ru.sdevteam.starlit.craft.buildings;

import org.json.JSONException;
import org.json.JSONObject;
import ru.sdevteam.starlit.craft.res.ResAmount;

/**
 * Class represents building that produces resources
 */
public class Plant extends Building
{
	protected ResAmount plantSpeed;
	protected ResAmount consumption;

	public Plant(JSONObject params) throws JSONException
	{
		super(params);
		plantSpeed = ResAmount.fromJSON(params.getJSONArray("plant_speed"));
		consumption = ResAmount.fromJSON(params.getJSONArray("plant_consumption"));
	}

	@Override
	public void update(long worldTime, int fullSecondsElapsed)
	{
		super.update(worldTime, fullSecondsElapsed);
		if (fullSecondsElapsed > 0)
		{
			if (system != null)
			{
				ResAmount withdrawn = system.withdrawAsMuchAsPossible(consumption.multiply(fullSecondsElapsed));
				ResAmount produced = new ResAmount();
				while (withdrawn.decreaseBy(consumption))
				{
					produced.increaseBy(plantSpeed);
				}
				system.storeAsMuchAsPossible(produced);
				// the rest of withdrawn resources that are not enough for producing one more plantSpeed
				system.storeAsMuchAsPossible(withdrawn);
			}
		}
	}

	@Override
	public Type getType()
	{
		return Type.PLANT;
	}
}

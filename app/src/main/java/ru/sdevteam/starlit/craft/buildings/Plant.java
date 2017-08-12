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
				// TODO: this may cause a bug because we withdraw all we can but cannot
				// guarantee that everything produced can be stored
				ResAmount produced = null;
				if (!consumption.isEmpty())
				{
					produced = new ResAmount();
					ResAmount withdrawn = system.withdrawAsMuchAsPossible(consumption.multiply(fullSecondsElapsed));
					while (withdrawn.decreaseBy(consumption))
					{
						produced.increaseBy(plantSpeed);
					}
					// the rest of withdrawn resources that are not enough for producing one more plantSpeed
					system.storeAsMuchAsPossible(withdrawn);
				}
				else
				{
					produced = plantSpeed.multiply(fullSecondsElapsed);
				}
				system.storeAsMuchAsPossible(produced);
			}
		}
	}

	@Override
	public Type getType()
	{
		return Type.PLANT;
	}
}

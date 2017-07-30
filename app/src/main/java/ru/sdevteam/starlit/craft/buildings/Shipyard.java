package ru.sdevteam.starlit.craft.buildings;

import org.json.JSONException;
import org.json.JSONObject;
import ru.sdevteam.starlit.craft.res.ResAmount;

/**
 * Created by bob on 30.07.17.
 */
public class Shipyard extends Building
{
	protected ResAmount shipPrice;
	protected int shipCount;

	public Shipyard(JSONObject params) throws JSONException
	{
		super(params);
		shipPrice = ResAmount.fromJSON(params.getJSONArray("ship_price"));
		shipCount = params.getInt("ships_amount");
	}
}

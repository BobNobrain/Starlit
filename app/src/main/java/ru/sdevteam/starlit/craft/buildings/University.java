package ru.sdevteam.starlit.craft.buildings;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by bob on 30.07.17.
 */
public class University extends Building
{
	protected float spSpeed;

	public University(JSONObject params) throws JSONException
	{
		super(params);
		spSpeed = (float) params.getDouble("sp_speed");
	}
}

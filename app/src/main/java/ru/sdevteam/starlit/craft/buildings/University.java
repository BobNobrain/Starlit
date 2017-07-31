package ru.sdevteam.starlit.craft.buildings;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class represents University building that scores science points
 */
public class University extends Building
{
	protected float spSpeed;

	public University(JSONObject params) throws JSONException
	{
		super(params);
		spSpeed = (float) params.getDouble("sp_speed");
	}

	@Override
	public Type getType()
	{
		return Type.SCIENCE;
	}
}

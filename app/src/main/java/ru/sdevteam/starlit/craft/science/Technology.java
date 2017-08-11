package ru.sdevteam.starlit.craft.science;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class represents a technology
 */
public class Technology
{
	public Technology(JSONObject params) throws JSONException
	{
		// TODO
	}

	public enum Category
	{
		COMMON,
		ENGINES,
		ARMOR,
		WEAPONS,
		SCIENCE;
		// ... and more (TODO) ...
	}

	private class Level
	{
		Level(int lvl, JSONObject params) throws JSONException
		{
			// TODO
		}
	}
}

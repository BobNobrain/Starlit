package ru.sdevteam.starlit.utils;


/**
 * Created by user on 11.07.2016.
 */
public class CorellationTable<R extends Enum<R>, C extends Enum<C>>
{
	public CorellationTable(Enum[] e1, Enum[] e2, float[][] corellation)
	{

	}

	private class Row
	{

	}

	private class Cell
	{
		Enum columnKey;
		float value;

		Cell(Enum ckey, float val)
		{
			columnKey = ckey;
			value = val;
		}
	}
}

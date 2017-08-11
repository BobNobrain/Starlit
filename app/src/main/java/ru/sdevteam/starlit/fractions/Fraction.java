package ru.sdevteam.starlit.fractions;

/**
 * Class represents one fraction and all its characteristics
 */
public class Fraction
{
	public String name;
	public int color;

	public Fraction(String name, int color)
	{
		this.name = name;
		this.color = color;
	}

	public boolean interplanetary;
	public boolean interstellar;
	public boolean spacecutters;
	public boolean wormholes;

	public float engineSpeed = 0F;
	public float shipCost = 1F;
	public float buildingCost = 1F;
	public float plantsSpeed = 1F;
	public float energyConsumption = 1F;
	public float destructionEfficiency = 1F;
}

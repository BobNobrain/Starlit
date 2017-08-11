package ru.sdevteam.starlit.fractions;

import ru.sdevteam.starlit.craft.science.TechTree;
import ru.sdevteam.starlit.craft.science.Technology;

/**
 * Class represents one fraction and all its characteristics
 */
public class Fraction
{
	public String name;
	public int color;

	public static final Fraction PLAYER = new Fraction("Player", 0xFF00FF00);

	private TechTree techTree;

	public Fraction(String name, int color)
	{
		this.name = name;
		this.color = color;
		this.techTree = new TechTree(this);
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

	/**
	 * Tries to learn a technology for this fraction
	 * @param t a technology to learn
	 * @return success or not
	 */
	public boolean learnTechnology(Technology t)
	{
		// TODO
		return true;
	}

	/**
	 * Upgrades learnt technology for this fraction
	 * @param t a technology to upgrade
	 */
	public void upgradeTechnology(Technology t)
	{
		// TODO
	}
}

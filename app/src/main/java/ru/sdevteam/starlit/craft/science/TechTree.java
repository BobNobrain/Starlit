package ru.sdevteam.starlit.craft.science;

import ru.sdevteam.starlit.fractions.Fraction;

/**
 * Class represents actual technology state for a fraction
 */
public class TechTree
{
	private Fraction f;

	public TechTree(Fraction owner)
	{
		// TODO
		f = owner;
	}

	/**
	 * Tries to learn a technology for owner fraction
	 * @param t a technology to learn
	 * @return success or not
	 */
	public boolean learn(Technology t)
	{
		// TODO
		return false;
	}

	/**
	 * Upgrades learnt technology for owner fraction
	 * @param t a technology to upgrade
	 */
	public void upgradeTechnology(Technology t)
	{
		// TODO
	}
}

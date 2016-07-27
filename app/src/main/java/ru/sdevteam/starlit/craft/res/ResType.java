package ru.sdevteam.starlit.craft.res;

/**
 * Created by user on 11.07.2016.
 */
public enum ResType
{
	METAL		(ResClass.ORE),
	CRYSTALS	(ResClass.ORE),
	PLUTONIUM	(ResClass.ORE),
	FUEL		(ResClass.GAS),
	RARE_GAS	(ResClass.GAS),
	FOOD		(ResClass.BIO),
	ENERGY		(ResClass.ENERGY)
	;

	public ResClass resClass;
	ResType(ResClass c)
	{
		resClass = c;
	}
}

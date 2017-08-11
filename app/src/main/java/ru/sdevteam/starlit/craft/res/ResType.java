package ru.sdevteam.starlit.craft.res;

/**
 * Resource types
 */
public enum ResType
{
	METAL		(ResClass.ORE,		'M'),
	CRYSTALS	(ResClass.ORE,		'C'),
	PLUTONIUM	(ResClass.ORE,		'P'),
	FUEL		(ResClass.GAS,		'U'),
	RARE_GAS	(ResClass.GAS,		'R'),
	FOOD		(ResClass.BIO,		'F'),
	ENERGY		(ResClass.ENERGY,	'Z')
	;

	public ResClass resClass;
	public char icon;
	ResType(ResClass c, char i)
	{
		resClass = c;
		icon = i;
	}
}

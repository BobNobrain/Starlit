package ru.sdevteam.starlit.ui;

import ru.sdevteam.starlit.craft.buildings.Building;

/**
 * This component is used in BuildInterface as representation of a building
 */
public class BuildingCheckbox extends Checkbox
{
	protected Building building;

	public BuildingCheckbox(Building b, int x, int y, int w, int h)
	{
		super(new DynamicFoneComponent(x, y, w, h));
		building = b;
	}

	public Building getBuilding() { return building; }
}

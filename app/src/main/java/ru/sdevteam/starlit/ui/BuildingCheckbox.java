package ru.sdevteam.starlit.ui;

import ru.sdevteam.starlit.craft.buildings.Building;

/**
 * This component is used in BuildInterface as representation of a building
 */
public class BuildingCheckbox extends Checkbox
{
	protected Building building;

	public BuildingCheckbox(Building b, int w, int h)
	{
		super(new DynamicFoneComponent(0, 0, w, h));
		building = b;
		setText(b.getName());
	}

	public Building getBuilding() { return building; }
}

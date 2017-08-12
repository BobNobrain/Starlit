package ru.sdevteam.starlit.ui;

import ru.sdevteam.starlit.craft.buildings.Building;
import ru.sdevteam.starlit.craft.buildings.BuildingSystem;

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

	public void defineColor(BuildingSystem.BuildFailedReason ground, BuildingSystem.BuildFailedReason orbit)
	{
		if (ground == null && orbit == null)
		{
			setBackgroundColor(32, 255, 255, 255);
			setTextColor(255, 255, 255, 255);
			return;
		}
		if (ground == BuildingSystem.BuildFailedReason.NONE ||
			orbit  == BuildingSystem.BuildFailedReason.NONE)
		{
			setBackgroundColor(32, 32, 255, 64);
			setTextColor(255, 32, 255, 64);
			return;
		}
		if (ground == BuildingSystem.BuildFailedReason.NOT_ENOUGH_PLACE ||
			orbit == BuildingSystem.BuildFailedReason.NOT_ENOUGH_PLACE)
		{
			setBackgroundColor(32, 128, 128, 128);
			setTextColor(255, 128, 128, 128);
			return;
		}
		setBackgroundColor(32, 255, 64, 32);
		setTextColor(255, 255, 64, 32);
	}
}

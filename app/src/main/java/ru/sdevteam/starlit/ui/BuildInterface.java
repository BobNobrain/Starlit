package ru.sdevteam.starlit.ui;

import ru.sdevteam.starlit.craft.buildings.Building;
import ru.sdevteam.starlit.craft.buildings.BuildingsRegistry;

/**
 * Class represents interface that allows user build in build systems
 */
public class BuildInterface extends CompoundComponent
{
	private int panelW;
	private Checkbox mainButton;
	private UIComponent buildGButton, buildOButton;
	private CompoundComponent toggleableContainer;
	private ListComponent categories, buildings;
	private CompoundComponent infoPanel;
	private Label buildingDescription;
	private boolean isActive;

	public BuildInterface(int x, int y, int width, int height, int panelWidth)
	{
		super(x, y, width, height);
		panelW = panelWidth;
		initComponents();
	}

	private void initComponents()
	{
		mainButton = new Checkbox(new DynamicFoneComponent(getX() + getWidth() - panelW - 2, getY() + 2, panelW, panelW));
		mainButton.subscribeStateChange(new Checkbox.StateChangedListener()
		{
			@Override
			public void onStateChanged(boolean newState)
			{
				toggleInterface(newState);
			}
		});

		int h = panelW / 2;
		int listPadding = 2;
		int textPadding = 5;

		toggleableContainer = new CompoundComponent(
			getX(),
			getY() + panelW,
			getWidth() - panelW,
			getHeight() - panelW
		);

		// categories list
		categories = new ListComponent(
			toggleableContainer.getX(),
			toggleableContainer.getY(),
			panelW,
			toggleableContainer.getHeight(),
			listPadding, true
		);
		for (int i = 0; i < 7; i++)
		{
			categories.appendChild(new Checkbox(new DynamicFoneComponent(0, 0, panelW, panelW, "" + i)));
		}
		CheckboxGroup catsCBG = new CheckboxGroup(categories);
		catsCBG.inspectCheckboxes();

		categories.appendChild(catsCBG);

		// buildings list
		buildings = new ListComponent(
			categories.getX() + categories.getWidth(),
			toggleableContainer.getY(),
			panelW * 3,
			toggleableContainer.getHeight(),
			listPadding, true
		);
		for (Building b: BuildingsRegistry.getForCategory(Building.Type.STORAGE))
		{
			buildings.appendChild(new BuildingCheckbox(b, 1, panelW * 2 / 3));
		}
//		for (int i = 0; i < 15; i++)
//		{
//			buildings.appendChild(
//				new Checkbox(new DynamicFoneComponent(0, 0, 1, panelW * 2 / 3, "Building " + i))
//			);
//		}
		CheckboxGroup bsCBG = new CheckboxGroup(buildings);
		bsCBG.inspectCheckboxes();
		bsCBG.subscribeStateChanged(onBuildingSelected);

		buildings.appendChild(bsCBG);

		// info panel
		infoPanel = new CompoundComponent(
			buildings.getX() + buildings.getWidth(),
			toggleableContainer.getY(),
			toggleableContainer.getWidth() - buildings.getWidth() - categories.getWidth(),
			toggleableContainer.getHeight()
		);
		buildGButton = new Button(
			infoPanel.getX(),
			infoPanel.getY() + infoPanel.getHeight() - h,
			infoPanel.getWidth() / 2,
			h,
			"Build at ground"
		);
		buildOButton = new Button(
			buildGButton.getX() + buildGButton.getWidth(),
			buildGButton.getY(),
			infoPanel.getWidth() / 2,
			h,
			"Build at orbit"
		);
		buildingDescription = new Label(
			infoPanel.getX(),
			infoPanel.getY(),
			infoPanel.getWidth(),
			infoPanel.getHeight() - buildGButton.getHeight(),
			"Select a building..."
		);
		buildingDescription.setPadding(textPadding);
		buildingDescription.setFormatted(true);
		infoPanel.appendChild(buildingDescription);
		infoPanel.appendChild(buildGButton);
		infoPanel.appendChild(buildOButton);

		// linking everything into container
		toggleableContainer.appendChild(categories);
		toggleableContainer.appendChild(buildings);
		toggleableContainer.appendChild(infoPanel);

		appendChild(mainButton);
//		appendChild(toggleableContainer);
	}

	private void toggleInterface(boolean state)
	{
		isActive = state;
		if (state)
		{
			appendChild(toggleableContainer);
		}
		else
		{
			removeChild(toggleableContainer);
		}
	}

	private void showDescription(Building b)
	{
		buildingDescription.setText(String.format("@u;%1$s@d;\n\n%2$s", b.getName(), b.getDescription()));
	}

	private CheckboxGroup.StateChangedListener onBuildingSelected = new CheckboxGroup.StateChangedListener()
	{
		@Override
		public void onStateChanged(Checkbox newChecked)
		{
			BuildingCheckbox b = (BuildingCheckbox) newChecked;
			showDescription(b.getBuilding());
		}
	};
}

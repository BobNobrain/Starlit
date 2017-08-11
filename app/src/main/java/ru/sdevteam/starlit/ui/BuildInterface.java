package ru.sdevteam.starlit.ui;

import ru.sdevteam.starlit.SelectionChangedEvent;
import ru.sdevteam.starlit.craft.buildings.Building;
import ru.sdevteam.starlit.craft.buildings.BuildingSystem;
import ru.sdevteam.starlit.craft.buildings.BuildingsRegistry;
import ru.sdevteam.starlit.craft.buildings.PlanetBuildingSystem;
import ru.sdevteam.starlit.world.BuildingSystemProvider;

/**
 * Class represents interface that allows user build in build systems
 */
public class BuildInterface extends CompoundComponent implements SelectionChangedEvent.Listener
{
	private GameUI root;

	private int panelW;
	private Checkbox mainButton;
	private Button buildGButton, buildOButton;
	private CompoundComponent toggleableContainer;
	private ListComponent categories, buildings;
	private CheckboxGroup bsCBG;
	private CompoundComponent infoPanel;
	private Label buildingDescription;

	private boolean isActive;

	private Building selectedBuilding;
	private Building.Type selectedCategory;

	public BuildInterface(GameUI root, int x, int y, int width, int height, int panelWidth)
	{
		super(x, y, width, height);
		panelW = panelWidth;
		this.root = root;
		root.getSelectionChangedEvent().subscribe(this);
		initComponents();

		showCategory(Building.Type.STORAGE);
		((CatCheckbox) categories.getChildren().get(0)).setState(true);
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
		for (Building.Type cat : Building.Type.values())
		{
			categories.appendChild(new CatCheckbox(cat, panelW));
		}
		CheckboxGroup catsCBG = new CheckboxGroup(categories);
		catsCBG.inspectCheckboxes();
		catsCBG.subscribeStateChanged(onCategorySelected);

		categories.appendChild(catsCBG);

		// buildings list
		buildings = new ListComponent(
			categories.getX() + categories.getWidth(),
			toggleableContainer.getY(),
			panelW * 3,
			toggleableContainer.getHeight(),
			listPadding, true
		);
		bsCBG = new CheckboxGroup(buildings);
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
		toggleableContainer.appendChild(new StaticFoneComponent(infoPanel));

		appendChild(mainButton);
//		appendChild(toggleableContainer);
	}

	@Override
	public void setTextSizeForAll(int val)
	{
		super.setTextSizeForAll(val);
		if (!isActive)
		{
			// is not appended to children, but textsize should be changed too
			toggleableContainer.setTextSizeForAll(val);
		}
	}

	private void toggleInterface(boolean state)
	{
		isActive = state;
		if (state)
		{
			appendChild(toggleableContainer);
			if (selectedBuilding != null)
			{
				root.getResPanel().showWithdraw();
			}
		}
		else
		{
			removeChild(toggleableContainer);
			root.getResPanel().hideWithdraw();
		}
	}

	private void showDescription(Building b)
	{
		if (b != null)
		{
			buildingDescription.setText(String.format("@u;%1$s@d;\n\n%2$s", b.getName(), b.getDescription()));
			PlanetBuildingSystem bs = obtainBuildingSystem();
			if (bs != null)
			{
				buildGButton.setEnabled(bs.getGround().canBuild(b) == BuildingSystem.BuildFailedReason.NONE);
				buildOButton.setEnabled(bs.getOrbit().canBuild(b) == BuildingSystem.BuildFailedReason.NONE);

				root.getResPanel().showWithdraw(b.getPrice());
			}
			else
			{
				buildGButton.setEnabled(false);
				buildOButton.setEnabled(false);
				root.getResPanel().hideWithdraw();
			}
		}
		else
		{
			buildingDescription.setText("Select a building");
			buildGButton.setEnabled(false);
			buildOButton.setEnabled(false);
			root.getResPanel().hideWithdraw();
		}
		selectedBuilding = b;
	}

	private CheckboxGroup.StateChangedListener onBuildingSelected = new CheckboxGroup.StateChangedListener()
	{
		@Override
		public void onStateChanged(Checkbox newChecked)
		{
			if (newChecked != null)
			{
				BuildingCheckbox b = (BuildingCheckbox) newChecked;
				showDescription(b.getBuilding());
			}
			else
			{
				showDescription(null);
			}
		}
	};

	private void showCategory(Building.Type cat)
	{
		selectedCategory = cat;
		if (cat == null) return;
		buildings.clear();
		bsCBG.clear();

		PlanetBuildingSystem bs = obtainBuildingSystem();

		for (Building b : BuildingsRegistry.getForCategory(cat))
		{
			buildings.appendChild(createBuildingCheckbox(b, bs));
		}
		bsCBG.inspectCheckboxes();
		buildings.setTextSizeForAll(getTextSize());
	}

	private BuildingCheckbox createBuildingCheckbox(Building b, PlanetBuildingSystem bs)
	{
		BuildingCheckbox result = new BuildingCheckbox(b, 1, panelW * 2 / 3);
		if (bs == null)
		{
			result.defineColor(null, null);
		}
		else
		{
			result.defineColor(bs.getGround().canBuild(b), bs.getOrbit().canBuild(b));
		}
		return result;
	}

	private CheckboxGroup.StateChangedListener onCategorySelected = new CheckboxGroup.StateChangedListener()
	{
		@Override
		public void onStateChanged(Checkbox newChecked)
		{
			if (newChecked != null)
			{
				CatCheckbox c = (CatCheckbox) newChecked;
				showCategory(c.cat);
			}
			else
			{
				showCategory(null);
			}
		}
	};

	@Override
	public void onSelectionChanged(Object newSelection)
	{
		// redraw list and info panel to represent actual relation to selected object
		showCategory(selectedCategory);
		showDescription(selectedBuilding);
	}

	private PlanetBuildingSystem obtainBuildingSystem()
	{
		Object selected = root.getSelectedObject();
		if (selected instanceof BuildingSystemProvider)
		{
			return ((BuildingSystemProvider) selected).getBuildingSystem();
		}
		else
		{
			return null;
		}
	}


	private class CatCheckbox extends Checkbox
	{
		Building.Type cat;

		CatCheckbox(Building.Type t, int panelW)
		{
			super(new DynamicFoneComponent(0, 0, panelW, panelW, t.name()));
			cat = t;
		}
	}
}

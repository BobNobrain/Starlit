package ru.sdevteam.starlit.ui;

/**
 * Class represents interface that allows user build in build systems
 */
public class BuildInterface extends CompoundComponent
{
	private int panelW;
	private Checkbox mainButton;
	private UIComponent buildButton;
	private CompoundComponent toggleableContainer;
	private boolean isActive;

	public BuildInterface(int x, int y, int width, int height, int panelWidth)
	{
		super(x, y, width, height);
		panelW = panelWidth;
		initComponents();
	}

	private void initComponents()
	{
		mainButton = new Checkbox(new DynamicFoneComponent(width - panelW - 2, 2, panelW, panelW));
		mainButton.subscribeStateChange(new Checkbox.StateChangedListener()
		{
			@Override
			public void onStateChanged(boolean newState)
			{
				toggleInterface(newState);
			}
		});

		buildButton = new DynamicFoneComponent(
			x + 2, y + height - panelW / 2 - 2,
			panelW * 2, panelW / 2,
			"Build"
		);

		toggleableContainer = new CompoundComponent(x, y, width, height);

		CheckboxGroup chG = new CheckboxGroup(toggleableContainer);

		UIComponent[] fakeButtons = new UIComponent[] {
			new Checkbox(new DynamicFoneComponent(x, y, panelW, panelW)),
			new Checkbox(new DynamicFoneComponent(x + panelW, y, panelW, panelW)),
			new Checkbox(new DynamicFoneComponent(x + 2*panelW, y, panelW, panelW))
		};

		for (UIComponent c: fakeButtons)
		{
			toggleableContainer.appendChild(c);
		}
		toggleableContainer.appendChild(buildButton);
		toggleableContainer.appendChild(chG);

		// now it's time for chG to search for checkboxes inside container and take 'em under control
		chG.inspectCheckboxes();

		appendChild(mainButton);
		appendChild(toggleableContainer);
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
}

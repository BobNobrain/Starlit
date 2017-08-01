package ru.sdevteam.starlit.ui;

/**
 * Class holds control over the whole game ui
 */
public final class GameUI extends CompoundComponent
{
	private int buttonsSize;

	private BuildInterface buildInterface;
	private CompoundComponent resourcesPanel;

	public GameUI(int screenWidth, int screenHeight, int buttonsSize)
	{
		super(0, 0, screenWidth, screenHeight);
		this.buttonsSize = buttonsSize;
		initComponents();
	}

	private void initComponents()
	{
		int m = 5;
		buildInterface = new BuildInterface(
			buttonsSize/2,
			m,
			getWidth() - buttonsSize / 2 - m,
			getHeight() - 2*m,
			buttonsSize
		);

		appendChild(buildInterface);
	}
}

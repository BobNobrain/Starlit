package ru.sdevteam.starlit.ui;

/**
 * Class holds control over the whole game ui
 */
public final class GameUI extends CompoundComponent
{
	private int buttonsSize;

	private BuildInterface buildInterface;
	private CompoundComponent resourcesPanel;
	private Label status;

	public GameUI(int screenWidth, int screenHeight, int buttonsSize, int textSize)
	{
		super(0, 0, screenWidth, screenHeight);
		this.buttonsSize = buttonsSize;
		initComponents();
		setTextSizeForAll(textSize);
	}

	private void initComponents()
	{
		int m = 5;

		status = new Label(m, getHeight() - textSize - m, getWidth() - 2*m, textSize + 2*m);
		status.setFormatted(true);

		buildInterface = new BuildInterface(
			buttonsSize/2,
			m,
			getWidth() - buttonsSize / 2 - m,
			getHeight() - 2*m - status.getHeight(),
			buttonsSize
		);
		buildInterface.setTextSize(textSize);

		appendChild(status);
		appendChild(buildInterface);
	}

	public void setStatusText(String text)
	{
		status.setText(text);
	}
}

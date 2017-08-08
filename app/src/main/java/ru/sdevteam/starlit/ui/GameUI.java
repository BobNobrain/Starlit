package ru.sdevteam.starlit.ui;

import ru.sdevteam.starlit.RenderView;
import ru.sdevteam.starlit.SelectionChangedEvent;
import ru.sdevteam.starlit.SelectionProvider;

/**
 * Class holds control over the whole game ui
 */
public final class GameUI extends CompoundComponent implements SelectionProvider
{
	private int buttonsSize;

	private RenderView renderView;

	private BuildInterface buildInterface;
	private CompoundComponent resourcesPanel;
	private Label status;

	public GameUI(RenderView rv, int screenWidth, int screenHeight, int buttonsSize, int textSize)
	{
		super(0, 0, screenWidth, screenHeight);
		renderView = rv;
		this.buttonsSize = buttonsSize;
		setTextSize(textSize);
		initComponents();
		setTextSizeForAll(textSize);
	}

	private void initComponents()
	{
		int m = 5;

		status = new Label(m, getHeight() - getTextSize() - 2*m, getWidth() - 2*m, getTextSize() + 2*m);
		status.setFormatted(true);

		buildInterface = new BuildInterface(
			this,
			buttonsSize/2,
			m,
			getWidth() - buttonsSize / 2 - m,
			getHeight() - 2*m - status.getHeight(),
			buttonsSize
		);
		buildInterface.setTextSize(getTextSize());

		appendChild(status);
		appendChild(buildInterface);
	}

	public void setStatusText(String text)
	{
		status.setText(text);
	}

	public Object getSelectedObject()
	{
		return renderView.getSelectedObject();
	}

	@Override
	public SelectionChangedEvent getSelectionChangedEvent()
	{
		return renderView.getSelectionChangedEvent();
	}
}

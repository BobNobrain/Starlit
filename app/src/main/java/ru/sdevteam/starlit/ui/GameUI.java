package ru.sdevteam.starlit.ui;

import ru.sdevteam.starlit.RenderView;
import ru.sdevteam.starlit.SelectionChangedEvent;
import ru.sdevteam.starlit.SelectionProvider;
import ru.sdevteam.starlit.craft.buildings.IStorage;
import ru.sdevteam.starlit.world.StorageAPIProvider;

/**
 * Class holds control over the whole game ui
 */
public final class GameUI extends CompoundComponent implements
													SelectionProvider,
													SelectionChangedEvent.Listener
{
	private int buttonsSize;

	private RenderView renderView;

	private BuildInterface buildInterface;
	private ResourcePanel resPanel;
	private Label status;

	public GameUI(RenderView rv, int screenWidth, int screenHeight, int buttonsSize, int textSize)
	{
		super(0, 0, screenWidth, screenHeight);
		renderView = rv;
		this.buttonsSize = buttonsSize;
		setTextSize(textSize);
		initComponents();
		setTextSizeForAll(textSize);

		rv.getSelectionChangedEvent().subscribe(this);
	}

	private void initComponents()
	{
		int m = 5;

		status = new Label(m, getHeight() - getTextSize() - 2*m, getWidth() - 2*m, getTextSize() + 2*m);
		status.setFormatted(true);

		resPanel = new ResourcePanel(m, m, getWidth() - 2*m, getTextSize(), m);

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
		appendChild(resPanel);
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

	public ResourcePanel getResPanel() { return resPanel; }


	@Override
	public void onSelectionChanged(Object newSelection)
	{
		if (newSelection instanceof StorageAPIProvider)
		{
			IStorage s = ((StorageAPIProvider) newSelection).getStorageAPI();
			resPanel.showResources(s.getTotalResourcesStored());
		}
	}
}

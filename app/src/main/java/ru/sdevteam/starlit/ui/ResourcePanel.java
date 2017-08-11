package ru.sdevteam.starlit.ui;

import android.graphics.Paint;
import ru.sdevteam.starlit.craft.res.ResAmount;
import ru.sdevteam.starlit.utils.Drawing;

/**
 * Component renders a resource panel (with panel of withdraw)
 */
public final class ResourcePanel extends CompoundComponent
{
	// TODO: 7x2 labels (or just render it manually)
	private Label main, withdraw;
	private boolean wShown;

	public ResourcePanel(int x, int y, int width, int textSize, int margin)
	{
		super(x, y, width, textSize * 2 + margin);

		main = new Label(x, y, width, textSize);
		main.setFont(Drawing.symbolFont);
		main.setTextAlign(Paint.Align.CENTER);
		main.setText(new ResAmount(0).toString());

		withdraw = new Label(x, y + textSize + margin, width, textSize);
		withdraw.setFont(Drawing.symbolFont);
		withdraw.setTextAlign(Paint.Align.CENTER);

		appendChild(main);
	}

	public void showResources(ResAmount r)
	{
		main.setText(r.toString());
	}
	public void showWithdraw(ResAmount r)
	{
		withdraw.setText(r.toString());
		if (!wShown)
		{
			wShown = true;
			appendChild(withdraw);
		}
	}
	public void hideWithdraw()
	{
		removeChild(withdraw);
		wShown = false;
	}
	public void showWithdraw()
	{
		if (!wShown)
		{
			wShown = true;
			appendChild(withdraw);
		}
	}

	@Override
	public void setTextSizeForAll(int val)
	{
		super.setTextSizeForAll(val);
		if (!wShown)
			withdraw.setTextSize(val);
	}
}

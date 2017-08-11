package ru.sdevteam.starlit.ui;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import ru.sdevteam.starlit.utils.Drawing;

/**
 * Component demonstrates a text label
 */
public class Label extends UIComponent
{
	public static final String DEFAULT_LABEL_TEXT = "Unnamed label";

	protected Paint.Align textAlign = Paint.Align.LEFT;
	public Paint.Align getTextAlign() { return textAlign; }
	public void setTextAlign(Paint.Align value) { textAlign = value; }

	private int padding;
	public int getPadding() { return padding; }
	public void setPadding(int value) { padding = value; }

	private boolean formatted;
	public boolean isFormatted() { return formatted; }
	public void setFormatted(boolean value) { formatted = value; }

	public Label()
	{
		setText(DEFAULT_LABEL_TEXT);
		bgColor = Color.TRANSPARENT;
	}

	public Label(int w, int h)
	{
		super(0, 0, w, h, DEFAULT_LABEL_TEXT);
		bgColor = Color.TRANSPARENT;
	}

	public Label(int x, int y, int w, int h)
	{
		super(x, y, w, h, DEFAULT_LABEL_TEXT);
		bgColor = Color.TRANSPARENT;
	}

	public Label(int x, int y, int w, int h, String text)
	{
		super(x, y, w, h, text);
		bgColor = Color.TRANSPARENT;
	}


	@Override
	protected Paint getTextPaint()
	{
		Paint p = super.getTextPaint();
		p.setTextAlign(textAlign);
		return p;
	}


	@Override
	protected void paintText(Canvas c)
	{
		if ((!isTextFittingSingleLine() || formatted) && (textAlign == Paint.Align.LEFT))
		{
			paintTextMultiline(c);
			return;
		}

		Paint.FontMetricsInt fm = getMetrics();
		int penX = getX() + padding, penY = getY() - fm.ascent + padding;
		switch (textAlign)
		{
			case CENTER:
				penX = getX() + getWidth() / 2;
				break;

			case RIGHT:
				penX = getX() + getWidth() - padding;
		}

		c.drawText(getText(), penX, penY, getTextPaint());
	}

	private void paintTextMultiline(Canvas c)
	{
		assert textAlign == Paint.Align.LEFT: "Multiline text with alignment different from LEFT is not implemented :(";
		c.save();
		c.translate(getX()+padding, getY()+padding);
		Drawing.paintMultilineText(getText(), getWidth()-2*padding, c, getTextPaint());
		c.restore();
	}

	private boolean isTextFittingSingleLine()
	{
		return getWidth() < getTextPaint().measureText(getText());
	}
}

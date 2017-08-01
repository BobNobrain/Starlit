package ru.sdevteam.starlit.utils;

import android.content.res.AssetManager;
import android.graphics.*;

import java.util.StringTokenizer;

/**
 * Created by user on 11.07.2016.
 */
public class Drawing
{
	public static Path triangleUp(float height)
	{
		float side = height / MathUtils.F_SQ3DIV2;
		Path triangle = new Path();
		triangle.moveTo(0, -height/2);
		triangle.lineTo(side/2, height/2);
		triangle.lineTo(-side/2, height/2);
		triangle.lineTo(0, -height/2);
		return triangle;
	}

	public static Path energy(float height)
	{
		float unit = height * MathUtils.F_1DIV14;
		Path zip = new Path();
		zip.moveTo(-3*unit, -7*unit);
		zip.lineTo(3*unit, -7*unit);
		zip.lineTo(unit, -3*unit);
		zip.lineTo(5*unit, -unit);
		zip.lineTo(-5*unit, height/2);
		zip.lineTo(-unit, unit);
		zip.lineTo(-5*unit, -unit);
		zip.lineTo(-3*unit, -7*unit);
		return zip;
	}


	private static final String MODIFIER = "@";
	private static final String MODIFIER_STOP = ";";

	static boolean debug = true;

	public static void paintMultilineText(String text, int width, Canvas c, Paint p)
	{
		Paint.FontMetricsInt fm = p.getFontMetricsInt();
		// assuming start point to be (0; 0)
		int penX = 0, penY = -fm.ascent;

		int lineHeight = fm.leading - fm.ascent + fm.descent;

		Paint rp = new Paint(p);
		// no processing for text alignment
		rp.setTextAlign(Paint.Align.LEFT);

		StringTokenizer sequence = new StringTokenizer(text, "\n "+MODIFIER+MODIFIER_STOP, true);
		String token;
		boolean flagComing = false, flagProcessed = false;
		while(sequence.hasMoreTokens())
		{
			token = sequence.nextToken();

			if (token.equals("\n"))
			{
				penX = 0;
				penY += lineHeight;
				continue;
			}

			if(token.equals(MODIFIER_STOP) && flagProcessed) // @A|;
			{
				flagProcessed = false;
				continue;
			}

			if(flagComing == token.equals(MODIFIER)) // @|@ or A|A => simply draw token
			{
				int tokenWidth = (int) rp.measureText(token);
				if(penX + tokenWidth > width)
				{
					// horizontal bounds overflow expected, moving caret onto new line
					penY += lineHeight;
					penX = 0;
				}

				c.drawText(token, penX, penY, rp);
				// moving caret
				penX += tokenWidth;

				flagComing = false;
			}
			else // @|A or A|@
			{
				if(token.equals(MODIFIER)) // A|@
				{
					flagComing = true;
				}
				else // @|A
				{
					applyFlag(token, rp, p);
					flagComing = false;
					flagProcessed = true;
				}
			}
		}
	}
	private static void applyFlag(String flag, Paint p ,Paint def)
	{
		if(flag.length() == 1)
		{
			if(flag.charAt(0) == '0')
			{
				p.setColor(def.getColor());
				return;
			}
			if(flag.charAt(0) >= '1' && flag.charAt(0) <= '9')
			{
				p.setColor(FLAG_COLORS[flag.charAt(0) - '0']);
				return;
			}
			if(flag.charAt(0) >='A' && flag.charAt(0) <='F')
			{
				p.setColor(FLAG_COLORS[flag.charAt(0) - 'A' + 10]);
				return;
			}
			if(flag.charAt(0) == 'u')
			{
				p.setUnderlineText((p.getFlags() & Paint.UNDERLINE_TEXT_FLAG) == 0);
				return;
			}
			if(flag.charAt(0) == 'd')
			{
				p.setColor(def.getColor());
				p.setFlags(def.getFlags());
			}
		}
	}
	private static final int[] FLAG_COLORS = {
			-1,				// 0 - default color
			Color.RED,		// 1
			Color.GREEN,	// 2
			Color.BLUE,		// 3
			Color.YELLOW,	// 4
			Color.CYAN,		// 5
			Color.MAGENTA,	// 6
			0xFF800000,		// 7 - maroon
			0xFF008000,		// 8 - dark green
			0xFF000080,		// 9 - navy
			0xFFFF4040,		// A - light red
			0xFF40FF40,		// B - light green
			0xFF4040FF,		// C - light blue
			Color.BLACK,	// D
			Color.GRAY,		// E
			Color.WHITE		// F
	};

	//
	// fonts
	//

	public static Typeface mainFont, symbolFont;
	public static void initFonts(AssetManager assets)
	{
		mainFont = Typeface.createFromAsset(assets, "fonts/jura.ttf");
		symbolFont = Typeface.createFromAsset(assets, "fonts/symbols.ttf");
	}
}

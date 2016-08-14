package ru.sdevteam.starlit.utils;

/**
 * Created by user on 27.06.2016.
 */
public class MathUtils
{
	public static final float F_PI  = 3.141592653589793F;
	public static final float F_2PI = 6.283185307179586F;
	public static final float F_SQ3DIV2 = 0.8660254037844386F;
	public static final float F_1DIV14 = 1/14F;

	public static float dist2Between(float x1, float y1, float x2, float y2)
	{
		float dx = x1 - x2, dy = y1 - y2;
		return dx*dx + dy*dy;
	}

	public static boolean pointInRect(float px, float py, float rx, float ry, float rw, float rh)
	{
		if(px<rx) return false;
		if(py<ry) return false;
		if(px>rx+rw) return false;
		if(px>ry+rh) return false;
		return true;
	}
	public static boolean pointInRect(int px, int py, int rx, int ry, int rw, int rh)
	{
		if(px<rx) return false;
		if(py<ry) return false;
		if(px>rx+rw) return false;
		if(px>ry+rh) return false;
		return true;
	}
}

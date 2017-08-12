package ru.sdevteam.starlit.world;

/**
 * Sector is a square area of space with all star systems located within
 */
public class Sector
{
	public static final int SECTOR_SIZE = 1000;
	public static final int MAX_STARS_PER_SECTOR = 30;
	public static final int MIN_STARS_PER_SECTOR = 15;

	private int x, y;
	public int getX() { return x; }
	public int getY() { return y; }

	private World world;
	public long getWorldTime() { return world.getWorldTimeMillis(); }

	private String name;
	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	private Sector left, right, top, bottom;
	public Sector atTop() { return top; }
	public Sector atLeft() { return left; }
	public Sector atRight() { return right; }
	public Sector atBottom() { return bottom; }
	public void linkTop(Sector s) { top=s; s.bottom=this; }
	public void linkLeft(Sector s) { left=s; s.right=this; }
	public void linkRight(Sector s) { right=s; s.left=this; }
	public void linkBottom(Sector s) { bottom=s; s.top=this; }
	public void disposeTop() { top.onDisposed(); top=null; }
	public void disposeLeft() { left.onDisposed(); left=null; }
	public void disposeRight() { right.onDisposed(); right=null; }
	public void disposeBottom() { bottom.onDisposed(); bottom=null; }

	private Star[] stars;
	public Star[] getStarsArray() { return stars; }


	public Sector(World w, int x, int y)
	{
		this.x = x; this.y = y;
		this.name = x+":"+y;
		stars = new Star[MAX_STARS_PER_SECTOR];

		world = w;
	}


	private void onDisposed()
	{

	}

	public void save()
	{

	}

	public void update()
	{
		for (Star s: stars)
		{
			if (s != null)
			{
				s.update();
			}
		}
	}
}

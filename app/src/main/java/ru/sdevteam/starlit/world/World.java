package ru.sdevteam.starlit.world;

import ru.sdevteam.starlit.craft.buildings.BuildingsRegistry;
import ru.sdevteam.starlit.craft.buildings.PlanetBuildingSystem;
import ru.sdevteam.starlit.craft.res.ResAmount;
import ru.sdevteam.starlit.craft.res.ResClass;
import ru.sdevteam.starlit.fractions.Fraction;

import java.util.Arrays;
import java.util.Random;
import java.util.Vector;

/**
 * Class stores all world data
 */
public class World
{
	public static final int ORIGIN_X = 0, ORIGIN_Y = 0;
	private static final int MIN_PLANETS_IN_HOME_SYSTEM = 4;
	private Vector<Sector> activeSectors;
	private Sector currentSector; // needed?
	private WorldGenerator generator;
	private long startTime, timeOffset;
	private Star homeStar;
	private Planet homePlanet;


	public World(long seed)
	{
		generator = new WorldGenerator(seed);
		activeSectors = new Vector<>();

		currentSector = generator.generateSector(0, 0);
		activeSectors.add(currentSector);
		currentSector.setName("Home sector");

		createHomePlanet(currentSector);
		conquerHomePlanet();

		startTime = System.currentTimeMillis();
		timeOffset = 0; // while not loading from file
	}


	public void update()
	{
		for (Sector s: activeSectors)
		{
			if (s != null)
			{
				s.update();
			}
		}
	}

	public Sector[] getVisibleSectors(int viewportX, int viewportY)
	{
		// assuming that max. 4 sectors are visible (coz image is scaling so inside renderview)
		Sector base = getOrGenerateSectorContaining(viewportX, viewportY);

		ensureRight(base);
		ensureBottom(base);
		ensureRight(base.atBottom());

		return new Sector[] { base, base.atRight(), base.atBottom(), base.atBottom().atRight() };
	}

	private Sector getOrGenerateSectorContaining(int worldX, int worldY)
	{
		int x = (worldX - ORIGIN_X) / Sector.SECTOR_SIZE;
		int y = (worldY - ORIGIN_Y) / Sector.SECTOR_SIZE;
		if(worldX<0) --x;
		if(worldY<0) --y;
		Sector found = getSectorByCoords(x, y);
		if(found == null)
		{
			found = generator.generateSector(x, y);
			activeSectors.add(found);
		}
		return found;
	}

	private boolean ensureRight(Sector base)
	{
		if(base.atRight() != null) return true;
		Sector generated = generator.generateSector(base.getX()+1, base.getY());
		activeSectors.add(generated);
		return false;
	}
	private boolean ensureBottom(Sector base)
	{
		if(base.atBottom() != null) return true;
		Sector generated = generator.generateSector(base.getX(), base.getY()+1);
		activeSectors.add(generated);
		return false;
	}

	private Sector getSectorByCoords(int x, int y)
	{
		for (Sector s : activeSectors)
		{
			if(s.getX() == x && s.getY() == y)
			{
				return s;
			}
		}
		return null;
	}

	private void disposeSectors()
	{

	}

	private Sector loadSector(int sectorX, int sectorY)
	{
		return null;
	}

	private void createHomePlanet(Sector in)
	{
		homeStar = null;
		homePlanet = null;

		// trying to find suitable star
		for (Star s: in.getStarsArray())
		{
			if (s == null) continue;
			if (s.getOrbit().length >= MIN_PLANETS_IN_HOME_SYSTEM && s.getAvailableResources().isAvailable(ResClass.BIO))
			{
				homeStar = s;
				break;
			}
		}
		// failed it, generate a brand new one
		if (homeStar == null)
		{
			homeStar = in.getStarsArray()[0];
			homeStar.changeName("Home Star G");
			if (homeStar.getOrbit().length < MIN_PLANETS_IN_HOME_SYSTEM)
			{
				homeStar.initOrbit(MIN_PLANETS_IN_HOME_SYSTEM);
				int[] distances = generator.generateDistances(MIN_PLANETS_IN_HOME_SYSTEM);
				for(int i = 0; i < MIN_PLANETS_IN_HOME_SYSTEM; i++)
				{
					Planet planet = generator.generatePlanet(homeStar, i, distances[i]);
					homeStar.placeOnOrbit(i, distances[i], planet, generator.r.nextFloat() > 0.9F);
				}
			}
		}
		else
			homeStar.changeName("Home Star");

		// trying to find a suitable planet
		for (Celestial c: homeStar.getOrbit())
		{
			if (c instanceof Planet)
			{
				Planet p = (Planet) c;
				if (p.getAvailableResources().isAvailable(ResClass.BIO))
				{
					homePlanet = p;
					break;
				}
			}
		}
		// failed it, no problem, let's create our own
		if (homePlanet == null)
		{
			int dist = homeStar.getOrbitDistances()[2];
			do
			{
				homePlanet = generator.generatePlanet(homeStar, 2, dist);
			}
			while (!homePlanet.getAvailableResources().isAvailable(ResClass.BIO));
			homeStar.placeOnOrbit(2, dist, homePlanet, false);
			homePlanet.changeName("Home Planet G");
		}
		else
			homePlanet.changeName("Home Planet");
	}

	private void conquerHomePlanet()
	{
		homePlanet.owner = Fraction.PLAYER;
		PlanetBuildingSystem bs = homePlanet.getBuildingSystem();
		bs.getGround().forceBuild(BuildingsRegistry.forId("b.government"));
		bs.storeAsMuchAsPossible(new ResAmount(11, 6, 0, 2, 0, 10, 0));
	}

	private class WorldGenerator
	{
		long seed;
		Random r;
		WorldGenerator(long s)
		{
			seed = s;
			r = new Random(seed);
		}

		Sector generateSector(int atX, int atY)
		{
			Sector generated = new Sector(World.this, atX, atY);
			Sector	left 	= getSectorByCoords(atX-1, atY),
					right	= getSectorByCoords(atX+1, atY),
					top		= getSectorByCoords(atX, atY-1),
					bottom	= getSectorByCoords(atX, atY+1);

			if(top != null) generated.linkTop(top);
			if(left != null) generated.linkLeft(left);
			if(right != null) generated.linkRight(right);
			if(bottom != null) generated.linkBottom(bottom);

			int starsCount = Sector.MIN_STARS_PER_SECTOR +
					r.nextInt(Sector.MAX_STARS_PER_SECTOR - Sector.MIN_STARS_PER_SECTOR);

			Star[] stars = generated.getStarsArray();
			for(int i=0; i<starsCount; i++)
			{
				stars[i] = generateStarSystem(generated);
			}

			return generated;
		}

		Star generateStarSystem(Sector parent)
		{
			StarType t = null;
			float p = r.nextFloat();
			for(StarType st: StarType.values())
			{
				if(p < st.spawnAbility)
				{
					t = st;
					break;
				}
				else
				{
					p -= st.spawnAbility;
				}
			}

			// TODO: make stars not to intersect
			int sx = r.nextInt(Sector.SECTOR_SIZE),
				sy = r.nextInt(Sector.SECTOR_SIZE);
			Star result = new Star(parent, sx, sy, t, false);

			// special distribution for planets count
			int planetsCount = 0;
			float psaFactor = 1 - t.emptyProb;
			while(r.nextFloat() < psaFactor)
			{
				planetsCount++;
				psaFactor -= t.psaDec;
			}
			result.initOrbit(planetsCount);

			//int lastOrbitDistance = Star.MIN_ORBIT_DISTANCE;
			int[] distances = generateDistances(planetsCount);
			for(int i=0; i<planetsCount; i++)
			{
				//int range = (Star.MAX_ORBIT_DISTANCE - lastOrbitDistance - Star.MIN_ORBIT_MARGIN)/(planetsCount-i);
				//int distance = lastOrbitDistance + r.nextInt(range);
				//System.out.println("Generated dist "+distance);

				Planet planet = generatePlanet(result, i, distances[i]);
				result.placeOnOrbit(i, distances[i], planet, r.nextFloat() > 0.9F);

				//lastOrbitDistance = distance + Star.MIN_ORBIT_MARGIN;
			}

			return result;
		}

		Planet generatePlanet(Star parent, int index, int dist)
		{
			Planet result = new Planet(parent.getSector(), 0, dist, parent, index);
			result.setMaterials(r.nextFloat(), r.nextFloat(),
					(float)(dist-Star.MIN_ORBIT_DISTANCE)/(Star.MAX_ORBIT_DISTANCE-Star.MIN_ORBIT_DISTANCE));
			result.spawnResources(r);
			// TODO: add moons
			return result;
		}

		int[] generateDistances(int count)
		{
			int[] distances = new int[count];

			for(int i=0; i<count; i++)
			{
				boolean generated = false;
				int d=0;
				while(!generated)
				{
					d = Star.MIN_ORBIT_DISTANCE + r.nextInt(Star.MAX_ORBIT_DISTANCE - Star.MIN_ORBIT_DISTANCE);
					generated=true;
					for (int j = 0; j < i; j++)
					{
						if (Math.abs(distances[j] - d)<Star.MIN_ORBIT_MARGIN)
						{
							generated = false;
							break;
						}
					}
				}
				distances[i]=d;
			}

			Arrays.sort(distances);

			return distances;
		}
	}

	public long getWorldTimeMillis()
	{
		return System.currentTimeMillis() - startTime + timeOffset;
	}
}

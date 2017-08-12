package ru.sdevteam.starlit.world;

import ru.sdevteam.starlit.craft.buildings.BuildingSystem;
import ru.sdevteam.starlit.craft.buildings.IStorage;
import ru.sdevteam.starlit.craft.buildings.PlanetBuildingSystem;
import ru.sdevteam.starlit.craft.res.ResAvailability;
import ru.sdevteam.starlit.craft.res.ResClass;
import ru.sdevteam.starlit.craft.res.ResType;
import ru.sdevteam.starlit.fractions.Fraction;
import ru.sdevteam.starlit.utils.MathUtils;

/**
 * A celestial body in the world. Can have satellites and is a key object of game process
 */
public abstract class Celestial implements
								BuildingSystemProvider,
								StorageAPIProvider
{
	protected float getOrbitSpeedCoeff() { return 500; }


	private int relX, relY;

	private float orbitalAngle;
	public float getOrbitalAngle() { return orbitalAngle; }
	public void setOrbitalAngle(float val) { orbitalAngle = val; }

	private Celestial[] orbit;
	public Celestial[] getOrbit() { return orbit; }

	private int[] orbitDistances;
	public int[] getOrbitDistances() { return orbitDistances; }

	// angular speeds
	private float[] orbitSpeeds;

	private Sector container;
	public Sector getSector()
	{
		return container;
	}

	private String name;
	public String getName() { return name; }
	public void changeName(String newName) { name = newName; }

	private ResAvailability availableResources;
	public ResAvailability getAvailableResources() { return availableResources; }
	public void enableResource(ResType rt) { availableResources.enable(rt); }
	public boolean isResourceAvailableInSystem(ResClass rc)
	{
		if(availableResources.isAvailable(rc)) return true;
		if(orbit == null) return false;
		for (int i = 0; i < orbit.length; i++)
		{
			if(orbit[i] != null)
			{
				if(orbit[i].isResourceAvailableInSystem(rc)) return true;
			}
		}
		return false;
	}

	protected PlanetBuildingSystem buildings;
	@Override
	public PlanetBuildingSystem getBuildingSystem()
	{
		return buildings;
	}

	@Override
	public IStorage getStorageAPI() { return buildings; }

	public Fraction owner;


	public Celestial(Sector location, int sx, int sy, String name)
	{
		relX = sx; relY = sy;
		container = location;
		this.name = name;

		availableResources = new ResAvailability();
	}
	public Celestial(Sector location, int sx, int sy)
	{
		this(location, sx, sy, "Unnamed Celestial");
	}


	public int getWorldX() { return container.getX()*Sector.SECTOR_SIZE + relX; }
	public int getWorldY() { return container.getY()*Sector.SECTOR_SIZE + relY; }
	public int getX() { return relX; }
	public int getY() { return relY; }

	public void initOrbit(int orbitsCount)
	{
		orbit = new Celestial[orbitsCount];
		orbitDistances = new int[orbitsCount];
		orbitSpeeds = new float[orbitsCount];
	}

	public void placeOnOrbit(int orbitIndex, int orbitDist, Celestial body, boolean oppositeDirection)
	{
		if(orbitIndex<0 || orbitIndex>=orbit.length) throw new IndexOutOfBoundsException("No such orbit: "+orbitIndex);
		orbitDistances[orbitIndex] = orbitDist;
		orbit[orbitIndex] = body;

		orbitSpeeds[orbitIndex] = (float)(getOrbitSpeedCoeff() / Math.sqrt(orbitDist) / orbitDist);
		if(oppositeDirection) orbitSpeeds[orbitIndex] = -orbitSpeeds[orbitIndex];
	}

	protected long lastTimeUpdated = 0;
	public void update()
	{
		long time = getWorldTimeMillis();
		long dt = time - lastTimeUpdated;
		if(orbit != null)
		{
			for (int i = 0; i < orbit.length; i++)
			{
				Celestial sat = orbit[i];
				float speed = orbitSpeeds[i];
				int R = orbitDistances[i];
				//float angle = sat.orbitalAngle;

				sat.orbitalAngle += speed * dt;
				while (sat.orbitalAngle > MathUtils.F_2PI)
					sat.orbitalAngle -= MathUtils.F_2PI;
				while (sat.orbitalAngle < 0)
					sat.orbitalAngle += MathUtils.F_2PI;

				sat.relX = (int) (R * Math.cos(sat.orbitalAngle));
				sat.relY = (int) (R * Math.sin(sat.orbitalAngle));

				sat.update();
			}
		}

		if (buildings != null)
			buildings.update(time);

		lastTimeUpdated = time;
	}

	protected long getWorldTimeMillis() { return container.getWorldTime(); }
}

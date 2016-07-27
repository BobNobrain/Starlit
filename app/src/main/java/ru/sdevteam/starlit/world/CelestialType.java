package ru.sdevteam.starlit.world;

/**
 * Created by user on 23.06.2016.
 */
public enum CelestialType
{
	STAR {
		@Override public boolean isHabitable() { return false; }
	},
	PLANET {
		@Override public boolean isHabitable() { return true; }
	},
	ASTEROID {
		@Override public boolean isHabitable() { return false; }
	};

	public abstract boolean isHabitable();
}

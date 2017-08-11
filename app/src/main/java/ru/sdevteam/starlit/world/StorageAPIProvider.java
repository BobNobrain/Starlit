package ru.sdevteam.starlit.world;

import ru.sdevteam.starlit.craft.buildings.IStorage;

/**
 * Interface should be implemented by every class that can provide an object with storage API
 */
public interface StorageAPIProvider
{
	IStorage getStorageAPI();
}

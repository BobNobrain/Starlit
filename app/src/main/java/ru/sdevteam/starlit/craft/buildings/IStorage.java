package ru.sdevteam.starlit.craft.buildings;

import ru.sdevteam.starlit.craft.res.ResAmount;
import ru.sdevteam.starlit.craft.res.StorageLimit;

/**
 * Interface should be implemented by all classes providing storage API
 */
public interface IStorage
{
	ResAmount getTotalResourcesStored();
	StorageLimit getStorageLimit();
	ResAmount storeAsMuchAsPossible(ResAmount amount);
	boolean withdraw(ResAmount amount);
	ResAmount withdrawAsMuchAsPossible(ResAmount amount);
}

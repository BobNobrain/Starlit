package ru.sdevteam.starlit;

/**
 * Interface for all objects that can provide a selection
 */
public interface SelectionProvider
{
	Object getSelectedObject();
	SelectionChangedEvent getSelectionChangedEvent();
}

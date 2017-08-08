package ru.sdevteam.starlit;

import java.util.Vector;

/**
 * Simple class implementing event of selection change
 */
public class SelectionChangedEvent
{
	private Vector<Listener> listeners;

	public SelectionChangedEvent()
	{
		listeners = new Vector<>();
	}

	public void subscribe(Listener l)
	{
		listeners.add(l);
	}
	public void unsubscribe(Listener l)
	{
		listeners.remove(l);
	}

	public void invoke(Object selection)
	{
		for (Listener l: listeners)
		{
			l.onSelectionChanged(selection);
		}
	}

	public interface Listener
	{
		void onSelectionChanged(Object newSelection);
	}
}

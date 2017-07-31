package ru.sdevteam.starlit.ui;

import android.graphics.Canvas;

import java.util.List;
import java.util.Vector;

/**
 * Class represents invisible UI component that manipulates inner checkboxes of given
 * compound component like radiobuttons
 */
public class CheckboxGroup extends InvisibleComponent implements Checkbox.StateChangedListener
{
	protected Vector<Checkbox> checkboxes;
	public boolean allowSwitchOff;
	private Checkbox current;

	protected CompoundComponent target;

	protected List<StateChangedListener> listeners;

	public CheckboxGroup(CompoundComponent of)
	{
		target = of;
		checkboxes = new Vector<>();
	}

	private void setCurrent(Checkbox value)
	{
		if (current != null)
			current.unsubscribeStateChange(this);
		current = value;
		if (value != null)
			current.subscribeStateChange(this);
		invokeStateChanged();
	}

	@Override
	public void onStateChanged(boolean newState)
	{
		// this callback is called only for current checkbox
		if (!newState && !allowSwitchOff)
		{
			// no switching off is allowed, turn this checkbox back to switched state
			current.setState(true);
		}
	}

	public void inspectCheckboxes()
	{
		boolean metActive = false;
		for (UIComponent c: target.getChildren())
		{
			if (c.is(Checkbox.class))
			{
				final Checkbox cb = (Checkbox) ((DecoratorComponent) c).findOfType(Checkbox.class);

				// but if there already were one, skip it - it's ready
				if (checkboxes.contains(cb)) continue;

				checkboxes.add(cb);

				// so if we are looking through all checkboxes, let's also switch off them all except the first
				if (cb.getState())
				{
					if (metActive) cb.setState(false);
					else metActive = true;
				}

				// if any checkbox is checked in, it should become current and switch the old current off
				cb.subscribeStateChange(new Checkbox.StateChangedListener()
				{
					@Override
					public void onStateChanged(boolean newState)
					{
						if (newState)
						{
							// new is switched on => old should be switched off
							Checkbox oldCurrent = current;
							setCurrent(cb);
							if (oldCurrent != null && oldCurrent != cb)
							{
								oldCurrent.setState(false);
							}
						}
					}
				});
			}
		}
	}

	public void subscribeStateChanged(StateChangedListener l)
	{
		listeners.add(l);
	}
	public void unsubscribeStateChanged(StateChangedListener l)
	{
		listeners.remove(l);
	}

	protected void invokeStateChanged()
	{
		for (StateChangedListener l: listeners)
		{
			l.onStateChanged(current);
		}
	}

	public interface StateChangedListener
	{
		void onStateChanged(Checkbox newChecked);
	}
}

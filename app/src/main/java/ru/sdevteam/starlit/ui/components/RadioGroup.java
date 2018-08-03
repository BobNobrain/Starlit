package ru.sdevteam.starlit.ui.components;

import ru.sdevteam.starlit.core.GOComponent;

public class RadioGroup extends GOComponent {
    private boolean allowSwitchOff;
    private ButtonBehavior active;

    public RadioGroup(boolean allowSwitchOff) {
        this.allowSwitchOff = allowSwitchOff;
        active = null;
    }

    public boolean isActive(ButtonBehavior btn) {
        return active == btn;
    }

    public boolean onButtonToggled(ButtonBehavior which, boolean on) {
        if (which == active && !on) {
            // attempt to switch off radio group
            return allowSwitchOff;
        }
        if (on) {
            if (active != null)
                active.setToggled(false);
            active = which;
        }
        return true;
    }
}

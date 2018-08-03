package ru.sdevteam.starlit.ui.components;

import android.graphics.Color;

import ru.sdevteam.starlit.core.TapEvent;
import ru.sdevteam.starlit.utils.MathUtils;


public class ButtonBehavior extends Listenable {
    public static final int ACTIVATION_DECAY_SPEED = 200;

    public RadioGroup radioGroup;

    public Colorable background;

    private int initialColor;
    private boolean enabled;

    private boolean toggleable;
    private boolean toggled;
    private int toggledColor;

    public ButtonBehavior(Colorable backgroundComponent) {
        background = backgroundComponent;

        enabled = true;
        toggleable = false;
        toggled = false;
        radioGroup = null;
    }
    public ButtonBehavior(Colorable backgroundComponent, int toggledOnColor) {
        this(backgroundComponent);
        toggleable = true;
        toggledColor = toggledOnColor;
    }

    public void setEnabled(boolean value) {
        enabled = value;
        onStateChanged();
    }

    public void setToggled(boolean value) {
        if (!toggleable) return;
        boolean allowed = true;
        if (radioGroup != null) {
            allowed = radioGroup.onButtonToggled(this, value);
        }
        if (allowed) {
            toggled = value;
            onStateChanged();
        }
    }

    protected void onStateChanged() {
        if (!toggled && enabled) {
            restoreBgColor();
        } else {
            if (toggled && toggleable) {
                setBgColor(toggledColor);
            } else if (!enabled) {
                int sum = Color.red(background.color) + Color.green(background.color) + Color.blue(background.color);
                int disabledColor = Color.argb(Color.alpha(background.color), sum/5, sum/5, sum/5);
                setBgColor(disabledColor);
            }
        }
    }

    protected void setBgColor(int color) {
        if (background == null) return;
        initialColor = background.color;
        background.color = color;
    }
    protected void restoreBgColor() {
        if (background == null) return;
        if (toggleable && toggled) background.color = toggledColor;
        else background.color = initialColor;
    }

    private int ticks;
    protected int maxTicks = 15;
    @Override
    public void update(float dt)
    {
        super.update(dt);
        if (ticks > 0)
        {
            ticks -= (int) (dt * ACTIVATION_DECAY_SPEED);
            background.setColor(
                    MathUtils.towards(Color.alpha(initialColor), 255, ticks, maxTicks),
                    MathUtils.towards(Color.red(initialColor), 255, ticks, maxTicks),
                    MathUtils.towards(Color.green(initialColor), 255, ticks, maxTicks),
                    MathUtils.towards(Color.blue(initialColor), 255, ticks, maxTicks)
            );
        }
    }

    @Override
    public void onTap(TapEvent ev) {
        if (enabled) {
            super.onTap(ev);
            ticks = maxTicks;
            if (toggleable) {
                setToggled(!toggled);
            }
        }
    }
}

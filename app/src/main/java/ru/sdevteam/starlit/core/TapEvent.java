package ru.sdevteam.starlit.core;

public class TapEvent extends Event {
    public float x;
    public float y;

    public TapEvent(float _x, float _y) {
        x = _x;
        y = _y;
    }
}

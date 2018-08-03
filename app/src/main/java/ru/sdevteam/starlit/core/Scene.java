package ru.sdevteam.starlit.core;

public class Scene extends GameObject {
    private float globalTime;

    public Scene() {
        globalTime = 0F;
    }

    @Override
    public void update(float deltaTime) {
        globalTime += deltaTime;
        super.update(deltaTime);
    }

    @Override
    public void addChild(GameObject child) {
        super.addChild(child);
        child.root = this;
    }

    @Override
    public void setActive(boolean value) {
        super.setActive(value);
        if (!value) throw new RuntimeException("Cannot deactivate scene!");
    }
}

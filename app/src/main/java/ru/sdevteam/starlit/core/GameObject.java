package ru.sdevteam.starlit.core;

import android.graphics.Canvas;

import java.util.ArrayList;
import java.util.List;

public class GameObject implements IHookHandler {

    private static final int HOOK_LAUNCH = 0;
    private static final int HOOK_UPDATE = 1;
    private static final int HOOK_PAINT = 2;
    private static final int HOOK_TAP = 3;
    private static final int HOOK_DOUBLE_TAP = 4;
    private static final int HOOK_LONG_TAP = 5;
    private static final int HOOK_DRAG_START = 6;
    private static final int HOOK_DRAG = 7;
    private static final int HOOK_DRAG_END = 8;
    private static final int HOOK_STOP = 9;


    private List<IHookHandler> components;

    private GameObject parent;
    protected Scene root;
    private List<IHookHandler> children;

    public volatile float x, y, width, height;
    private volatile boolean active;
    private volatile boolean launched;

    public GameObject() {
        parent = null;
        components = new ArrayList<>();
        children = new ArrayList<>();
        x = 0F; y = 0F; width = 0F; height = 0F;
        active = true;
    }

    public GameObject getParent() {
        return parent;
    }

    public int childrenCount() {
        return children.size();
    }
    public GameObject getChild(int index) {
        return (GameObject) children.get(index);
    }

    public void addChild(GameObject child) {
        child.parent = this;
        children.add(child);
    }
    public void removeChild(GameObject which) {
        children.remove(which);
    }
    public void removeChildAt(int index) {
        children.remove(index);
    }

    public List<GOComponent> getComponents(Class base) {
        List<GOComponent> result = new ArrayList<>();
        for (int i = 0; i < components.size(); i++) {
            GOComponent c = (GOComponent) components.get(i);
            if (base.isAssignableFrom(c.getClass())) {
                result.add(c);
            }
        }
        return result;
    }
    public GOComponent getComponent(Class base) {
        for (int i = 0; i < components.size(); i++) {
            GOComponent c = (GOComponent) components.get(i);
            if (base.isAssignableFrom(c.getClass())) {
                return c;
            }
        }
        return null;
    }

    public void attachComponent(GOComponent component) {
        components.add(component);
        if (launched) {
            component.launch();
        }
    }

    protected void callHook(int hook, List<IHookHandler> objs) {
        for (int i = 0; i < objs.size(); i++) {
            IHookHandler obj = objs.get(i);
            if (obj.isActive()) {
                if (hook == HOOK_LAUNCH) {
                    obj.launch();
                } else if (hook == HOOK_STOP) {
                    obj.stop();
                } else {
                    throw new RuntimeException("Cannot call object hook: launch");
                }
            }
        }
    }
    protected void callHook(int hook, List<IHookHandler> objs, TapEvent event) {
        for (int i = 0; i < objs.size(); i++) {
            IHookHandler obj = objs.get(i);
            if (obj.isActive()) {
                switch (hook) {
                    case HOOK_TAP:
                        obj.onTap(event);
                        break;
                    case HOOK_DOUBLE_TAP:
                        obj.onDoubleTap(event);
                        break;
                    case HOOK_LONG_TAP:
                        obj.onLongTap(event);
                        break;
                    default:
                        throw new RuntimeException("Cannot call object hook: tap");
                }
            }
        }
    }
    protected void callHook(int hook, List<IHookHandler> objs, float dt) {
        for (int i = 0; i < objs.size(); i++) {
            IHookHandler obj = objs.get(i);
            if (obj.isActive()) {
                if (hook == HOOK_UPDATE) {
                    obj.update(dt);
                } else {
                    throw new RuntimeException("Cannot call object hook: update");
                }
            }
        }
    }
    protected void callHook(int hook, List<IHookHandler> objs, Canvas c) {
        for (int i = 0; i < objs.size(); i++) {
            IHookHandler obj = objs.get(i);
            if (obj.isActive()) {
                if (hook == HOOK_PAINT) {
                    obj.paint(c);
                } else {
                    throw new RuntimeException("Cannot call object hook: paint");
                }
            }
        }
    }
    protected void callHook(int hook, List<IHookHandler> objs, DragEvent event) {
        for (int i = 0; i < objs.size(); i++) {
            IHookHandler obj = objs.get(i);
            if (obj.isActive()) {
                switch (hook) {
                    case HOOK_DRAG_START:
                        obj.onDragStart(event);
                        break;
                    case HOOK_DRAG_END:
                        obj.onDragEnd(event);
                        break;
                    case HOOK_DRAG:
                        obj.onDrag(event);
                        break;
                    default:
                        throw new RuntimeException("Cannot call object hook: drag");
                }
            }
        }
    }

    @Override
    public boolean isActive() {
        return active;
    }
    public void setActive(boolean value) {
        active = value;
        if (!launched) {
            launch();
        }
    }

    @Override
    public void launch() {
        if (!active) return;
        callHook(HOOK_LAUNCH, components);
        callHook(HOOK_LAUNCH, children);
        launched = true;
    }

    public void stop() {
        if (!active) return;
        callHook(HOOK_STOP, components);
        callHook(HOOK_STOP, children);
    }

    public void update(float deltaTime) {
        if (!active) return;
        callHook(HOOK_UPDATE, components, deltaTime);
        callHook(HOOK_UPDATE, children, deltaTime);
    }
    public void paint(Canvas c) {
        if (!active) return;
        callHook(HOOK_PAINT, components, c);
        callHook(HOOK_PAINT, children, c);
    }

    public void onTap(TapEvent ev) {
        if (!active) return;
        callHook(HOOK_TAP, components, ev);
        callHook(HOOK_TAP, children, ev);
    }
    public void onDoubleTap(TapEvent ev) {
        if (!active) return;
        callHook(HOOK_DOUBLE_TAP, components, ev);
        callHook(HOOK_DOUBLE_TAP, children, ev);
    }
    public void onLongTap(TapEvent ev) {
        if (!active) return;
        callHook(HOOK_LONG_TAP, components, ev);
        callHook(HOOK_LONG_TAP, children, ev);
    }

    public void onDragStart(DragEvent ev) {
        if (!active) return;
        callHook(HOOK_DRAG_START, components, ev);
        callHook(HOOK_DRAG_START, children, ev);
    }
    public void onDrag(DragEvent ev) {
        if (!active) return;
        callHook(HOOK_DRAG, components, ev);
        callHook(HOOK_DRAG, children, ev);
    }
    public void onDragEnd(DragEvent ev) {
        if (!active) return;
        callHook(HOOK_DRAG_END, components, ev);
        callHook(HOOK_DRAG_END, children, ev);
    }
}

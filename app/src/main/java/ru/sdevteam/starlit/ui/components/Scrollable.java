package ru.sdevteam.starlit.ui.components;

import ru.sdevteam.starlit.core.DragEvent;
import ru.sdevteam.starlit.core.GOComponent;

public class Scrollable extends GOComponent {
    private float originX, originY;

    public float minX, maxX, minY, maxY;

    public Scrollable() {
        originX = originY = 0;
    }
    public Scrollable(float xMin, float xMax, float yMin, float yMax) {
        minX = xMin;
        maxX = xMax;
        minY = yMin;
        maxY = yMax;
    }

//    @Override
//    public void launch() {
//        super.launch();
//        originX = gameObject().x;
//        originY = gameObject().y;
//    }


    @Override
    public void onDragStart(DragEvent ev) {
        originX = gameObject().x;
        originY = gameObject().y;
    }

    @Override
    public void onDrag(DragEvent ev) {
        float dx = ev.currentX - ev.startX;
        float dy = ev.currentY - ev.startY;
        synchronized (this) {
            gameObject().x = Math.max(minX, Math.min(originX + dx, maxX));
            gameObject().y = Math.max(minY, Math.min(originY + dy, maxY));
        }
    }

//    @Override
//    public void onDragEnd(DragEvent ev) {
//        // TODO: inertial movement
//    }
}

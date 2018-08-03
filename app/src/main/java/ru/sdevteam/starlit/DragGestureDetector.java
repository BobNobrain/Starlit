package ru.sdevteam.starlit;

import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

import ru.sdevteam.starlit.core.DragEvent;

public class DragGestureDetector {
    private List<TouchPos> lastTouches;
    private DragGestureListener l;

    public DragGestureDetector(DragGestureListener listener) {
        l = listener;
        lastTouches = new ArrayList<>(5);
    }

    void onTouchEvent(MotionEvent ev) {
        final int action = MotionEventCompat.getActionMasked(ev);
        int pointerIndex;
        float x, y;
        switch (action) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                // first finger
                pointerIndex = MotionEventCompat.getActionIndex(ev);
                final int pointerId = ev.getPointerId(pointerIndex);
                x = MotionEventCompat.getX(ev, pointerIndex);
                y = MotionEventCompat.getY(ev, pointerIndex);

                // remember last touch
                TouchPos touchStart = getForPointerId(pointerId);
                touchStart.active = true;
                touchStart.x = x;
                touchStart.y = y;
                break;

            case MotionEvent.ACTION_MOVE:
                for (int i = 0; i < lastTouches.size(); i++) {
                    TouchPos pos = lastTouches.get(i);
                    if (!pos.active) continue;

                    pointerIndex = MotionEventCompat.findPointerIndex(ev, pos.pointerId);
                    float nx = MotionEventCompat.getX(ev, pointerIndex);
                    float ny = MotionEventCompat.getY(ev, pointerIndex);

                    if (!pos.dragStarted) {
                        float dx = nx - pos.x;
                        float dy = ny - pos.y;

                        float d2 = dx*dx + dy*dy;
                        if (d2 > 25F) {
                            // more then 5 pixels distance, time to start drag
                            pos.dragStarted = true;
                        }
                    } else {
                        // TODO
                    }
                }
                break;

            case MotionEvent.ACTION_POINTER_UP:
            case MotionEvent.ACTION_UP:
                break;
        }
    }

    private TouchPos getForPointerId(int pid) {
        for (int i = 0; i < lastTouches.size(); i++) {
            TouchPos result = lastTouches.get(i);
            if (result != null) {
                if (result.pointerId == pid) {
                    return result;
                }
            }
        }

        TouchPos newTp = new TouchPos(0, 0, pid);
        lastTouches.add(newTp);
        return newTp;
    }

    private static class TouchPos {
        float x;
        float y;
        int pointerId;
        boolean active;
        boolean dragStarted = false;
        TouchPos(float _x, float _y, int p) {
            x = x; y = _y; pointerId = p;
            active = true;
        }
    }

    public interface DragGestureListener {
        void onDragStart(DragEvent ev);
        void onDragEnd(DragEvent ev);
        void onDrag(DragEvent ev);
    }
}

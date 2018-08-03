package ru.sdevteam.starlit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import ru.sdevteam.starlit.core.DragEvent;
import ru.sdevteam.starlit.core.Scene;
import ru.sdevteam.starlit.core.SceneManager;
import ru.sdevteam.starlit.core.TapEvent;

/**
 * Created by user on 23.06.2016.
 */
public class RenderView
        extends SurfaceView
        implements
            SurfaceHolder.Callback,
            GestureDetector.OnGestureListener,
            GestureDetector.OnDoubleTapListener,
            SceneManager.SceneChangedListener,
            DragGestureDetector.DragGestureListener
{
	private Bitmap buffer;
	private Canvas bufferCanvas;
	private SurfaceHolder holder;
	private GestureDetector gd;
	private DragGestureDetector dragGd;

	private int realWidth, realHeight;

	private Timer paintTimer, updateTimer, fpsTimer;

	private final Object mutex = new Object();


	public RenderView(Context context, AttributeSet attrs)
	{
		super(context, attrs);

		holder = getHolder();
		holder.addCallback(this);

		gd = new GestureDetector(context, this);
		gd.setOnDoubleTapListener(this);

		dragGd = new DragGestureDetector(this);
	}

    @Override
    public void onSceneChanged(Scene newScene, Scene oldScene) {
        newScene.width = realWidth;
        newScene.height = realHeight;
    }

    private void initSizes() {
		realWidth = getWidth();
		realHeight = getHeight();

        Scene activeScene = SceneManager.getActiveScene();
		if (activeScene != null) {
            activeScene.width = realWidth;
            activeScene.height = realHeight;
        }

		buffer = Bitmap.createBitmap(realWidth, realHeight, Bitmap.Config.ARGB_8888);
		bufferCanvas = new Canvas(buffer);
		bufferCanvas.drawColor(Color.RED);

	}

	private void update(long dtMillis) {
	    float dtS = dtMillis / 1000F;
        synchronized (mutex) {
            Scene activeScene = SceneManager.getActiveScene();
            if (activeScene != null) activeScene.update(dtS);
        }
	}

	private void paint() {
        synchronized (mutex) {
            Scene activeScene = SceneManager.getActiveScene();
            if(activeScene == null) return;
            activeScene.paint(bufferCanvas);
		}

		Canvas screenCanvas = null;
		try {
			screenCanvas = holder.lockCanvas();
			synchronized (holder)
			{
				screenCanvas.drawBitmap(buffer, 0, 0, null);
			}
		}
		finally {
			if(screenCanvas != null)
				holder.unlockCanvasAndPost(screenCanvas);
		}
	}

	//
    // SURFACE HOLDER
    //

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		initSizes();
	}

	@Override
	public void surfaceCreated(SurfaceHolder surfaceHolder) {
		paintTimer = new Timer("Paint", new Timer.Listener() {
			@Override
			public void onTick(long dt)
			{
				paint();
			}
		}, 33);
		paintTimer.enableStats();

		updateTimer = new Timer("Update", new Timer.Listener() {
			@Override
			public void onTick(long dt) {
				update(dt);
			}
		}, 20);
		updateTimer.enableStats();

		fpsTimer = new Timer("FPS debug meter", new Timer.Listener() {
			@Override
			public void onTick(long dt) {
				int paFps = updateTimer.getMeanFrameDelay();
				System.out.println("Paint fps: " + (paFps == 0? "inf" : 1000 / paFps));
				int upFps = updateTimer.getMeanFrameDelay();
				System.out.println("Update fps: " + (upFps == 0? "inf" : 1000 / upFps));
			}
		}, 2000);

		paintTimer.start();
		updateTimer.start();
		fpsTimer.start();
	}

	@Override
	public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {}

	@Override
	public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
		paintTimer.stop();
		updateTimer.stop();
		fpsTimer.stop();

		paintTimer.surelyJoin();
		updateTimer.surelyJoin();
		fpsTimer.surelyJoin();
	}

	//
    // GESTURES
    //

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        gd.onTouchEvent(event);
        dragGd.onTouchEvent(event);
        return true;
    }

	@Override
	public boolean onSingleTapUp(MotionEvent ev) {
        Scene activeScene = SceneManager.getActiveScene();
	    if (activeScene != null)
	        activeScene.onTap(new TapEvent(ev.getX(), ev.getY()));
		return true;
	}

	@Override
	public void onLongPress(MotionEvent motionEvent) {
        Scene activeScene = SceneManager.getActiveScene();
	    if (activeScene != null)
	        activeScene.onLongTap(new TapEvent(motionEvent.getX(), motionEvent.getY()));
	}

	@Override
	public boolean onDoubleTap(MotionEvent ev) {
        Scene activeScene = SceneManager.getActiveScene();
	    if (activeScene != null)
	        activeScene.onDoubleTap(new TapEvent(ev.getX(), ev.getY()));
		return true;
	}

    @Override
    public void onDragStart(DragEvent ev) {
        Scene activeScene = SceneManager.getActiveScene();
        if (activeScene != null)
            activeScene.onDragStart(ev);
    }

    @Override
    public void onDrag(DragEvent ev) {
        Scene activeScene = SceneManager.getActiveScene();
        if (activeScene != null)
            activeScene.onDrag(ev);
    }

    @Override
    public void onDragEnd(DragEvent ev) {
        Scene activeScene = SceneManager.getActiveScene();
        if (activeScene != null)
            activeScene.onDragEnd(ev);
    }

    //
    // UNUSED
    //

    @Override
    public boolean onDown(MotionEvent motionEvent) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent motionEvent) {}

    @Override
    public boolean onScroll(MotionEvent ev1, MotionEvent ev2, float dx, float dy) {
        return false;
    }

    @Override
    public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
        return false;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
        return false;
    }

	@Override
	public boolean onDoubleTapEvent(MotionEvent motionEvent) {
		return false;
	}
}

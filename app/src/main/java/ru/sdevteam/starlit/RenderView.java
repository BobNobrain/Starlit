package ru.sdevteam.starlit;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import ru.sdevteam.starlit.ui.BuildInterface;
import ru.sdevteam.starlit.ui.CompoundComponent;
import ru.sdevteam.starlit.ui.DynamicFoneComponent;
import ru.sdevteam.starlit.ui.GameUI;
import ru.sdevteam.starlit.world.World;

/**
 * Created by user on 23.06.2016.
 */
public class RenderView extends SurfaceView implements
											SurfaceHolder.Callback,
											GestureDetector.OnGestureListener,
											GestureDetector.OnDoubleTapListener
{
	public static final float MIN_DRAG_DIST = 5F;
	private Paint p;

	private Bitmap buffer;
	private Canvas bufferCanvas;
	private SurfaceHolder holder;

	private GestureDetector gd;

	private int realWidth, realHeight;
	private int panelWidth;
	private int textSize;

	private Timer paintTimer, updateTimer, fpsTimer;

	private AbstractDisplay currentDisplay;
	private SectorsDisplay sectorsDisplay;
	// TODO: private StarDisplay lastStarDisplay;
	// mb? private GalaxyDisplay galaxyDisplay;

	private GameUI componentsRoot;

	private World currentWorld;


	public RenderView(Context context, AttributeSet attrs)
	{
		super(context, attrs);

		holder = getHolder();
		holder.addCallback(this);

		gd = new GestureDetector(context, this);
		gd.setOnDoubleTapListener(this);

		currentWorld = new World(1);
	}

	private void initSizes()
	{
		realWidth = getWidth();
		realHeight = getHeight();

		// TODO: define this magic values in more magical way
		panelWidth = realWidth / 10;
		textSize = realWidth / 40;
		componentsRoot = new GameUI(realWidth, realHeight, panelWidth, textSize);

		if(currentDisplay == null)
		{
			sectorsDisplay = new SectorsDisplay(currentWorld, realWidth, realHeight, componentsRoot);
			currentDisplay = sectorsDisplay;
		}

		buffer = Bitmap.createBitmap(realWidth, realHeight, Bitmap.Config.ARGB_8888);
		bufferCanvas = new Canvas(buffer);
		bufferCanvas.drawColor(Color.RED);

	}

	private void initPaints()
	{
		p = new Paint();
		p.setColor(Color.rgb(33, 155, 200));
	}

	private void update()
	{
		if(currentDisplay!=null)
		{
			synchronized (currentDisplay)
			{
				currentDisplay.update();
			}
			componentsRoot.update();
		}
	}

	private void paint()
	{
		if(currentDisplay == null) return;

		synchronized (currentDisplay)
		{
			currentDisplay.drawContent(bufferCanvas);
		}

		p.setStyle(Paint.Style.FILL);

		// all components lay above the display
		componentsRoot.paint(bufferCanvas);

		Canvas screenCanvas = null;
		try
		{
			screenCanvas = holder.lockCanvas();
			synchronized (holder)
			{
				screenCanvas.drawBitmap(buffer, 0, 0, null);
			}
		}
		finally
		{
			if(screenCanvas != null)
				holder.unlockCanvasAndPost(screenCanvas);
		}
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh)
	{
		super.onSizeChanged(w, h, oldw, oldh);

		initSizes();
		initPaints();
	}


	@Override
	public boolean onTouchEvent(MotionEvent event)
	{
		super.onTouchEvent(event);
		gd.onTouchEvent(event);
		return true;
	}


	@Override
	public void surfaceCreated(SurfaceHolder surfaceHolder)
	{
		paintTimer = new Timer("Paint", new Timer.Listener()
		{
			@Override
			public void onTick()
			{
				paint();
			}
		}, 33);
		paintTimer.enableStats();

		updateTimer = new Timer("Update", new Timer.Listener()
		{
			@Override
			public void onTick()
			{
				update();
			}
		}, 20);
		updateTimer.enableStats();

		fpsTimer = new Timer("FPS debug meter", new Timer.Listener()
		{
			@Override
			public void onTick()
			{
				int paFps = updateTimer.getMeanFrameDelay();
				System.out.println("Paint fps: " + (paFps==0? "inf" : 1000/paFps));
				int upFps = updateTimer.getMeanFrameDelay();
				System.out.println("Update fps: " + (upFps==0? "inf" : 1000/upFps));
			}
		}, 2000);

		paintTimer.start();
		updateTimer.start();
		fpsTimer.start();
	}

	@Override
	public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2)
	{

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder surfaceHolder)
	{
		paintTimer.stop();
		updateTimer.stop();
		fpsTimer.stop();

		boolean retry = true;
		while(retry)
		{
			try
			{
				paintTimer.getThread().join();
				retry = false;
			}
			catch (InterruptedException ex) {}
		}
		retry = true;
		while(retry)
		{
			try
			{
				updateTimer.getThread().join();
				retry = false;
			}
			catch (InterruptedException ex) {}
		}
		retry = true;
		while(retry)
		{
			try
			{
				fpsTimer.getThread().join();
				retry = false;
			}
			catch (InterruptedException ex) {}
		}
	}

	@Override
	public boolean onDown(MotionEvent motionEvent)
	{
		return false;
	}

	@Override
	public void onShowPress(MotionEvent motionEvent)
	{

	}

	@Override
	public boolean onSingleTapUp(MotionEvent ev)
	{
		if(!componentsRoot.invokeOnTap((int)ev.getX(), (int)ev.getY()))
		{
			synchronized (currentDisplay)
			{
				currentDisplay.selectObjectUnder(ev.getX(), ev.getY());
			}
		}
		return true;
	}

	@Override
	public boolean onScroll(MotionEvent ev1, MotionEvent ev2, float dx, float dy)
	{
		if(!componentsRoot.invokeOnScroll((int) ev1.getX(), (int) ev1.getY(), (int) dx, (int) dy))
		{
			synchronized (currentDisplay)
			{
				currentDisplay.moveViewportBy(dx, dy);
			}
		}
		return true;
	}

	@Override
	public void onLongPress(MotionEvent motionEvent)
	{
		componentsRoot.invokeOnLongTap((int)motionEvent.getX(), (int)motionEvent.getY());
	}

	@Override
	public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1)
	{
		return false;
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent motionEvent)
	{
		return false;
	}

	@Override
	public boolean onDoubleTap(MotionEvent ev)
	{
		// TODO: add transitions
		if(!componentsRoot.invokeOnDoubleTap((int)ev.getX(), (int)ev.getY()))
		{
			synchronized (currentDisplay)
			{
				AbstractDisplay d = currentDisplay.displayObjectUnder(ev.getX(), ev.getY());
				if (d != null)
				{
					currentDisplay = d;
				}
				else currentDisplay = sectorsDisplay;
			}
		}
		return true;
	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent motionEvent)
	{
		return false;
	}
}

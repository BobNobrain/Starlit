package ru.sdevteam.starlit;

/**
 * Created by user on 06.07.2016.
 */
public class Timer implements Runnable {
	private Thread t;
	public Thread getThread() { return t; }

	private boolean running;
	public boolean isRunning() { return running; }
	private int dt = 33;
	private Listener l;

	private String name;
	public String getName() { return name; }

	private int[] fpsStat;
	private int fpsStatIndex;
	private boolean statsEnabled;

	public Timer(String name, Listener listener) {
		t = new Thread(this);
		t.setDaemon(true);
		t.setName("Timer " + name);
		l = listener;
		running = false;
		this.name = name;

		statsEnabled = false;
	}

	public Timer(String name, Listener listener, int frameDuration) {
		this(name, listener);
		dt = frameDuration;
	}

	public void start() {
		running = true;
		t.start();
	}

	public void stop() {
		running = false;
	}

	@Override
	public void run() {
	    long lastWaited = 0L;
		while(running) {
			try {
				long time = System.currentTimeMillis();
				l.onTick(time - lastWaited);
				lastWaited = System.currentTimeMillis();
				long delta = lastWaited - time;
				if(delta<dt) {
					Thread.sleep(dt - delta);
				} else {
					// System.out.println(name + ": WARNING! Low fps detected!");
				}
				if(statsEnabled) {
					synchronized (fpsStat) {
						fpsStat[fpsStatIndex] = (int)delta;
					}
					++fpsStatIndex;
					if(fpsStatIndex >= fpsStat.length) fpsStatIndex = 0;
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (Exception e) {
				System.err.println("CRITICAL! Exception on Timer "+name);
				System.err.println("Exception says: " + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	public void enableStats() {
		if(statsEnabled) return;
		fpsStat = new int[50];
		fpsStatIndex = 0;
		statsEnabled = true;
	}
	public int getMeanFrameDelay() {
		if(!statsEnabled) return dt;

		int result = 0;
		for (int i=0; i<fpsStat.length; i++) {
			synchronized (fpsStat) {
				result += fpsStat[i];
			}
		}
		return result / fpsStat.length;
	}

	public void surelyJoin() {
	    boolean joined = false;
	    while (!joined) {
	        try {
	            t.join();
	            joined = true;
            } catch (InterruptedException ex) {}
        }
    }

	public interface Listener {
		void onTick(long dt);
	}
}

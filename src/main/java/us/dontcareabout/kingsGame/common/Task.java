package us.dontcareabout.kingsGame.common;

import java.util.Random;

public abstract class Task {
	private static final Random random = new Random();

	protected long interval;

	private int randomMax;
	protected long checkTime;

	public Task(int interval) {
		this(interval, 0);
	}

	public Task(int interval, int randomMax) {
		this.interval = interval * 1000;
		this.randomMax = randomMax;
		checkTime = Util.now();
	}

	public void check() {
		if (Util.now() - checkTime < interval) { return; }

		checkTime = Util.now() + (randomMax == 0 ? 0 : random.nextInt(randomMax) * 1000);
		process();
	}

	public void delay(int sec) {
		checkTime += sec * 1000;
	}

	protected abstract void process();
}

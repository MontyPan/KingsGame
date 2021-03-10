package us.dontcareabout.kingsGame.common;

public abstract class Task {
	protected long interval;
	private long checkTime;

	public Task(long interval) {
		this.interval = interval;
		checkTime = Util.now();
	}

	public void check() {
		if (Util.now() - checkTime < interval) { return; }

		checkTime = Util.now();
		process();
	}

	public void delay(long ms) {
		checkTime += ms;
	}

	protected abstract void process();
}

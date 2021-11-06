package us.dontcareabout.kingsGame.common;

import java.util.Date;
import java.util.Random;

public abstract class Task {
	private static final Random random = new Random();

	public final String name;

	/** 執行時的優先順序，越小越優先 */
	public final Integer priority;


	private int interval = 30;
	private int randomMax;
	private long nextTime;

	public Task(String name) {
		this(name, Integer.MAX_VALUE);
	}

	public Task(String name, int priority) {
		this.name = name;
		this.priority = priority;
		nextTime = Util.now();
	}

	protected abstract void process();

	/** 兩次 task 中間間隔的保底時間，單位 s，預設值 30 */
	public int getInterval() {
		return interval;
	}

	public void setInterval(int interval) {
		this.interval = interval;
	}

	/** 兩次 task 中間間隔的時間加成上限，單位 s */
	public int getRandomMax() {
		return randomMax;
	}

	public void setRandomMax(int randomMax) {
		this.randomMax = randomMax;
	}

	/** 下次執行的預計時間（{@link Date#getTime()} */
	public long getNextTime() {
		return nextTime;
	}

	/** 下次執行的預計時間 */
	public Date getNextDate() {
		return new Date(nextTime);
	}

	public void delay(int sec) {
		nextTime += sec * 1000;
	}

	public final void disable() {
		nextTime = Long.MAX_VALUE;
	}

	public final void enable() {
		resetNextTime();
	}

	public final void start() {
		process();
		resetNextTime();
	}

	private void resetNextTime() {
		nextTime = Util.now() +
			(interval + (randomMax == 0 ? 0 : random.nextInt(randomMax))) * 1000;
	}
}

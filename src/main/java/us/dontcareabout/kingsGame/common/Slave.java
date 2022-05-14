package us.dontcareabout.kingsGame.common;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;

public class Slave {
	private static Slave instance;
	static {
		System.setProperty("java.awt.headless", "false");
		try { instance = new Slave(); } catch(Exception e) {}
	}
	public static Slave call() {
		if (instance == null) { throw new IllegalStateException(); }
		return instance;
	}

	private final Robot robot;

	private Slave() throws Exception {
		robot = new Robot();
	}

	public void keyin(int key, int... moreKey) {
		//如果沒 delay，則組合 key 會發送失敗
		robot.keyPress(key);
		robot.delay(10);

		for (int k : moreKey) {
			robot.keyPress(k);
			robot.delay(10);
		}

		for (int i = moreKey.length - 1; i >= 0; i--) {
			robot.keyRelease(moreKey[i]);
			robot.delay(10);
		}

		robot.keyRelease(key);
	}

	public void move(XY xy) {
		robot.mouseMove(xy.x, xy.y);
	}

	public void click(XY xy) {
		move(xy);
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
	}

	public void doubleClick(XY xy) {
		click(xy);
		wait(100);
		click(xy);
	}

	public void pressAndMove(XY start, int distance) {
		int way = distance < 0 ? -1 : 1;

		move(start);
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
		robot.delay(500);
		for (int i = 1; i <= Math.abs(distance); i++) {
			robot.mouseMove(start.x, start.y + (i * way));
			robot.delay(80);
		}
		robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
	}

	public Color getColor(XY xy) {
		return robot.getPixelColor(xy.x, xy.y);
	}

	/**
	 * @param ms 不能超過 60000ms
	 */
	public void wait(int ms) {
		robot.delay(ms);
	}

	/**
	 * @param second 不能超過 60s
	 */
	public void sleep(int second) {
		wait(second * 1000);
	}

	public BufferedImage screenShot() {
		return robot.createScreenCapture(new Rectangle(Toolkit.getDefaultToolkit().getScreenSize()));
	}

	public BufferedImage screenShot(Rect area) {
		Rectangle screenRect = new Rectangle(area.location.x, area.location.y, area.size.x, area.size.y);
		return robot.createScreenCapture(screenRect);
	}
}

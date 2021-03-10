package us.dontcareabout.kingsGame.common;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;

public class Slave {
	private final Robot robot;

	public Slave() throws Exception {
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

	public void click(XY xy) {
		robot.mouseMove(xy.x, xy.y);
		robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
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

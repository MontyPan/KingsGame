package us.dontcareabout.kingsGame.qtd;

import java.awt.Color;
import java.util.Calendar;
import java.util.Date;

import us.dontcareabout.kingsGame.common.Slave;
import us.dontcareabout.kingsGame.common.Util;
import us.dontcareabout.kingsGame.common.XY;

/**
 * 座標、狀態、以及操作行為集散地。
 */
public class QtdSlave {
	private static final Slave slave = Slave.call();

	/** 在基本畫面中，點下去不會造成任何引想的點 */
	public static final XY safePoint = new XY(300, 300);

	private static final XY ascendButton = new XY(100, 340);
	private static final XY ascendConfirm = new XY(335, 495);
	private static final XY ascendJoinConfirm = new XY(70, 495);
	private static final XY ascendOffSeasonEnd = new XY(450, 400);
	private static final XY ascendOnSeasonEnd = new XY(450, 450);
	private static final XY ascendBlood = new XY(80, 320);

	/** 同時也是返回主畫面的按鈕 */
	private static final XY teamButton = new XY(35, 350);

	private static final XY[] crewXY = new XY[7];
	static {
		XY crew1 = new XY(25, 520);
		int crewWidth = 140;

		for (int i = 0; i < crewXY.length; i++) {
			crewXY[i] = new XY(crew1.x + i * crewWidth, crew1.y);
		}
	}
	private static final Color upgradeEnable = new Color(-4352430);
	/** 數值越大、顏色相異容忍度越大 */
	private static final int upgradeDiffThreshold = 90;


	/** {@link Slave#sleep(int)} */
	public static void sleep(int second) {
		slave.sleep(second);
	}

	public static boolean upgradeCrew(int index) {
		if (!isUpgradable(index)) { return false; }

		slave.click(crewXY[index]);
		return true;
	}

	/**
	 * @param i 值域：1～3
	 */
	public static void swapTeam(int i) {
		slave.click(teamButton);
		slave.sleep(3);
		slave.click(new XY(100 + 50 * (i - 1), 350));
		slave.sleep(3);
		slave.click(teamButton);
	}

	public static void doAscend() {
		slave.click(ascendButton);
		sleep(5);
		slave.click(isJoinAscend() ? ascendJoinConfirm : ascendConfirm);
		sleep(5);
		slave.click(isOffSeason() ? ascendOffSeasonEnd : ascendOnSeasonEnd);
	}

	public static boolean isOffSeason() {
		Calendar c = Calendar.getInstance();
		c.set(2021, 9, 25, 8, 0, 0);	//到 0800 之前都還是休賽狀態
		long startTime = c.getTimeInMillis();
		long nowTime = new Date().getTime();
		long value = (nowTime - startTime) / 86400000 % 14;
		return value == 0 || value == 1;
	}

	public static boolean isJoinAscend() {
		int color = slave.getColor(ascendBlood).getRGB();
		return color == -5629928;
	}

	public static boolean isUpgradable(int index) {
		return Util.colorDiff(slave.getColor(crewXY[index]), upgradeEnable) < upgradeDiffThreshold;
	}
}

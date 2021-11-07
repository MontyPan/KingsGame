package us.dontcareabout.kingsGame.qtd;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Calendar;
import java.util.Date;

import us.dontcareabout.kingsGame.common.Rect;
import us.dontcareabout.kingsGame.common.Slave;
import us.dontcareabout.kingsGame.common.Util;
import us.dontcareabout.kingsGame.common.XY;

/**
 * 座標、狀態、以及操作行為集散地。
 * <p>
 * 操作行為內可能會包含 sleep 時間，但操作結束後不會預留 sleep 時間。
 */
public class QtdSlave {
	private static final Slave slave = Slave.call();

	/** {@link Slave#sleep(int)} */
	public static void sleep(int second) {
		slave.sleep(second);
	}

	public static boolean isOffSeason() {
		Calendar c = Calendar.getInstance();
		c.set(2021, 9, 25, 8, 0, 0);	//到 0800 之前都還是休賽狀態
		long startTime = c.getTimeInMillis();
		long nowTime = new Date().getTime();
		long value = (nowTime - startTime) / 86400000 % 14;
		return value == 0 || value == 1;
	}

	// ======== 晉升（Ascend）區 ======== //
	private static final XY ascendButton = new XY(100, 340);
	private static final XY ascendConfirm = new XY(335, 495);
	private static final XY ascendJoinConfirm = new XY(70, 495);
	private static final XY ascendOffSeasonEnd = new XY(450, 400);
	private static final XY ascendOnSeasonEnd = new XY(450, 450);
	private static final XY ascendBlood = new XY(80, 320);

	public static void doAscend() {
		slave.click(ascendButton);
		sleep(5);
		slave.click(isJoinAscend() ? ascendJoinConfirm : ascendConfirm);
		sleep(5);
		slave.click(isOffSeason() ? ascendOffSeasonEnd : ascendOnSeasonEnd);
	}

	public static boolean isJoinAscend() {
		int color = slave.getColor(ascendBlood).getRGB();
		return color == -5629928;
	}
	// ================ //


	// ======== 升級倍數（LvX）區 ======== //
	private static int lvX = 0;
	private static final XY lvMultiple = new XY(170, 395);
	private static final Rect lvMultipleArea = new Rect(new XY(130, 380), new XY(80, 30));
	private static final BufferedImage[] lvMultipleImage = new BufferedImage[3];
	static {
		for (int i = 0; i < lvMultipleImage.length; i++) {
			lvMultipleImage[i] = Util.read("QTD/LvX" + i + ".png");
		}
	}

	/**
	 * @return 值域：0～2。代表 Lv 倍數為 10^n
	 */
	public static int getLvX() { return lvX; }

	/**
	 * @param n 值域：0～2。代表 Lv 倍數為 10^n
	 */
	public static void swapLvX(int n) {
		lvX = n;

		//為了預防畫面不是預期的樣子，所以只嘗試三次、避免無窮迴圈
		int count = 0;

		while(count != 3 && !Util.compare(lvMultipleImage[n], slave.screenShot(lvMultipleArea))) {
			slave.click(lvMultiple);
			slave.sleep(2);
			count++;
		}
	}
	// ================ //


	// ======== 角色升級區（Crew Upgrade）區 ======== //
	private static final XY[] crewXY = new XY[7];
	static {
		XY crew1 = new XY(25, 520);
		int crewWidth = 140;

		for (int i = 0; i < crewXY.length; i++) {
			crewXY[i] = new XY(crew1.x + i * crewWidth, crew1.y);
		}
	}

	public static boolean upgradeCrew(int index) {
		if (!isUpgradable(index)) { return false; }

		slave.click(crewXY[index]);
		return true;
	}

	private static final Color upgradeEnable = new Color(-4352430);
	/** 數值越大、顏色相異容忍度越大 */
	private static final int upgradeDiffThreshold = 90;

	public static boolean isUpgradable(int index) {
		return Util.colorDiff(slave.getColor(crewXY[index]), upgradeEnable) < upgradeDiffThreshold;
	}
	// ================ //


	// ======== 目前推關關卡比較（Stage Compare）區 ======== //
	private static final Rect levelArea = new Rect(440, 55, 35, 15);
	private static BufferedImage preStageImage;
	private static boolean stageDifferent = false;

	/**
	 * 比較目前關卡與上一次呼叫時是否有差異。
	 * 本身不會回傳結果，而是要用 {@link #isStageDifferent()}。
	 * 這樣就除了節省效率之外，
	 * 也不用擔心 {@link #isStageDifferent()} 短時間內重複呼叫造成的問題。
	 */
	public static void compareStage() {
		//第一次呼叫，preStageImage 會是空的，所以視為有差別
		if (preStageImage == null) {
			preStageImage = slave.screenShot(levelArea);
			stageDifferent = true;
			return;
		}

		BufferedImage nowLvImg = slave.screenShot(levelArea);
		stageDifferent = !Util.compare(preStageImage, nowLvImg);
		preStageImage = nowLvImg;
	}

	public static boolean isStageDifferent() {
		return stageDifferent;
	}
	// ================ //


	// ======== 切換編隊（Team）區 ======== //
	/** 同時也是返回主畫面的按鈕 */
	private static final XY teamButton = new XY(35, 350);

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
	// ================ //
}

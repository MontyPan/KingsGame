package us.dontcareabout.kingsGame.qtd;

import static us.dontcareabout.kingsGame.qtd.QtdCoord.ascendBlood;
import static us.dontcareabout.kingsGame.qtd.QtdCoord.ascendButton;
import static us.dontcareabout.kingsGame.qtd.QtdCoord.ascendConfirm;
import static us.dontcareabout.kingsGame.qtd.QtdCoord.ascendJoinConfirm;
import static us.dontcareabout.kingsGame.qtd.QtdCoord.ascendOffSeasonEnd;
import static us.dontcareabout.kingsGame.qtd.QtdCoord.ascendOnSeasonEnd;
import static us.dontcareabout.kingsGame.qtd.QtdCoord.bsDesktopIcon;
import static us.dontcareabout.kingsGame.qtd.QtdCoord.bsExit;
import static us.dontcareabout.kingsGame.qtd.QtdCoord.bsExitConfirm;
import static us.dontcareabout.kingsGame.qtd.QtdCoord.bsSaveEngry;
import static us.dontcareabout.kingsGame.qtd.QtdCoord.bsSaveEngryOn;
import static us.dontcareabout.kingsGame.qtd.QtdCoord.crewXY;
import static us.dontcareabout.kingsGame.qtd.QtdCoord.lvMultiple;
import static us.dontcareabout.kingsGame.qtd.QtdCoord.lvMultipleArea;
import static us.dontcareabout.kingsGame.qtd.QtdCoord.stageArea;
import static us.dontcareabout.kingsGame.qtd.QtdCoord.teamButton;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.Calendar;
import java.util.Date;

import us.dontcareabout.kingsGame.common.OCR.Lang;
import us.dontcareabout.kingsGame.common.Rect;
import us.dontcareabout.kingsGame.common.Slave;
import us.dontcareabout.kingsGame.common.Util;
import us.dontcareabout.kingsGame.common.XY;
import us.dontcareabout.kingsGame.server.Logger;
import us.dontcareabout.kingsGame.shared.qtd.Parameter;

/**
 * 座標、狀態（{@link State}）、以及操作行為集散地。
 * <p>
 * 操作行為內可能會包含 sleep 時間，但操作結束後不會預留 sleep 時間。
 */
public class QtdSlave {
	public static final State state = new State();

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

	// ======== 無主孤魂座標區 ======== //
	private static final Rect screen = new Rect(new XY(0, 41), new XY(Parameter.SCREEN_WIDTH, Parameter.SCREEN_HEIGHT));
	private static final XY safePoint = new XY(350, 300);
	// ================ //

	// ======== BlueStack 區 ======== //
	public static void exitBlueStacks() {
		Logger.log("退出 BlueStacks");
		slave.click(bsExit);
		slave.sleep(2);
		slave.click(bsExitConfirm);
	}

	/**
	 * 執行 QTD 到「離線統計」對話框消失
	 */
	public static void enterQTD() {
		//等遊戲 loading 完進入遊戲
		slave.sleep(35);
		//要再點一下才會真的進入遊戲
		slave.click(safePoint);
		slave.sleep(20);	//懶得點，等待離線統計視窗自己消失
	}

	public static void swapSaveEngry() {
		slave.click(bsSaveEngry);
		slave.sleep(2);
		slave.click(bsSaveEngryOn);
		slave.move(safePoint);	//要移開才能讓 panel 消失
	}

	public static void restartQTD() {
		Logger.log("重新啟動 QTD");
		slave.sleep(3);	//避免本機一按下去就作動，減少沒點到的可能
		exitBlueStacks();
		slave.sleep(30);
		Logger.log("執行 QTD");
		slave.doubleClick(bsDesktopIcon);
		enterQTD();
		swapSaveEngry();
		swapLvX(state.getLvX());
	}
	// ================ //

	// ======== 晉升（Ascend）區 ======== //
	public static void doAscend() {
		slave.click(ascendButton);
		sleep(10);
		slave.click(isJoinAscend() ? ascendJoinConfirm : ascendConfirm);
		sleep(15);
		slave.click(isOffSeason() ? ascendOffSeasonEnd : ascendOnSeasonEnd);
	}

	public static boolean isJoinAscend() {
		//曾經出現過的歷史值 -6481375, -5629928, -6219752, -6418399
		return Util.colorDiff(
			slave.getColor(ascendBlood), new Color(-6418399)
		) < 20;
	}
	// ================ //


	// ======== 升級倍數（LvX）區 ======== //
	private static final BufferedImage[] lvMultipleImage = new BufferedImage[3];
	static {
		for (int i = 0; i < lvMultipleImage.length; i++) {
			lvMultipleImage[i] = Util.read("QTD/LvX" + i + ".png");
		}
	}

	/**
	 * @param n 值域：0～2。代表 Lv 倍數為 10^n
	 */
	public static void swapLvX(int n) {
		state.lvX = n;

		//為了預防畫面不是預期的樣子，所以只嘗試三次、避免無窮迴圈
		int count = 0;

		while(count != 3 && !comparePlus(lvMultipleImage[n], lvMultipleArea)) {
			slave.click(lvMultiple);
			slave.sleep(2);
			count++;
		}
	}
	// ================ //


	// ======== 角色升級區（Crew Upgrade）區 ======== //
	public static boolean upgradeCrew(int index) {
		if (!state.isUpgradable(index)) { return false; }

		slave.click(crewXY[index]);
		return true;
	}

	private static final Color upgradeEnable = new Color(-4352430);
	private static final Color upgradeEnd = new Color(-10251587);

	/** 數值越大、顏色相異容忍度越大 */
	private static final int upgradeDiffThreshold = 90;
	// ================ //

	// ======== 目前推關關卡比較（Stage Compare）區 ======== //
	/**
	 * 比較目前關卡與上一次呼叫時是否有差異。
	 * 本身不會回傳結果，而是要用 {@link #isStageDifferent()}。
	 * 這樣就除了節省效率之外，
	 * 也不用擔心 {@link State#isStageDifferent()} 短時間內重複呼叫造成的問題。
	 */
	public static void compareStage() {
		//第一次呼叫，preStageImage 會是空的，所以視為有差別
		if (state.preStageImage == null) {
			state.preStageImage = slave.screenShot(stageArea);
			state.stageDifferent = true;
			return;
		}

		BufferedImage nowLvImg = slave.screenShot(stageArea);
		state.stageDifferent = !Util.ocrCompare(state.preStageImage, nowLvImg);
		state.preStageImage = nowLvImg;
	}
	// ================ //


	// ======== 切換編隊（Team）區 ======== //
	/**
	 * @param i 值域：1～3
	 */
	public static void swapTeam(int i) {
		slave.click(teamButton);
		slave.sleep(3);
		slave.click(new XY(100 + 50 * (i - 1), 350));
		slave.sleep(3);
		slave.click(teamButton);
		state.team = i;
	}

	private static final BufferedImage[] teamActiveImage = new BufferedImage[3];
	static {
		for (int i = 0; i < teamActiveImage.length; i++) {
			teamActiveImage[i] = Util.read("QTD/Team" + (i + 1) + ".png");
		}
	}

	private static final XY teamSize = new XY(38, 38);

	/**
	 * @param i 值域：1～3
	 */
	public static Rect getTeamArea(int i) {
		return new Rect(new XY(83 + (i - 1) * 52, 323), teamSize);
	}
	// ================ //


	public static class State {
		private BufferedImage preStageImage;
		private boolean stageDifferent = false;

		private int lvX;

		private int team = 0;
		private int[][] upgradeIndex = {
			{2, 3},
			{3, 4, 6},
			{3, 6, 5, 4, 1, 2}
		};

		/**
		 * @return 值域：0～2。代表 Lv 倍數為 10^n
		 */
		public int getLvX() { return lvX; }

		public BufferedImage getPreStageImage() {
			return preStageImage;
		}

		public String getPreStage() {
			return Util.ocr(preStageImage, Lang.en);
		}

		public BufferedImage getScreenImage() {
			return slave.screenShot(screen);
		}

		/**
		 * @return 值域：1～3
		 */
		public int getTeam() {
			if (team != 0) { return team; }

			slave.click(teamButton);
			slave.sleep(3);

			for (int i = 0; i < teamActiveImage.length; i++) {
				Rect rect = getTeamArea(i + 1);
				if (comparePlus(teamActiveImage[i], rect)) {
					slave.click(teamButton);
					team = i + 1;
					return team;
				}
			}

			slave.click(teamButton);
			Logger.log("Team 偵測出錯");
			throw new IllegalStateException("Team 偵測出錯");
		}

		public int[] getUpgradeIndex() {
			return upgradeIndex[getTeam() - 1];
		}

		public void setUpgradeIndex(int[] index) {
			upgradeIndex[getTeam() - 1] = index;
		}

		public boolean isStageDifferent() {
			return stageDifferent;
		}

		public boolean isUpgradable(int index) {
			return Util.colorDiff(slave.getColor(crewXY[index]), upgradeEnable) < upgradeDiffThreshold;
		}

		public boolean isUpgradeEnd(int index) {
			return Util.colorDiff(slave.getColor(crewXY[index]), upgradeEnd) < upgradeDiffThreshold;
		}
	}

	// ======== utility method 區 ======== //
	/**
	 * 這是為了解決 BlueStack 有時候會被 Windows 莫名移動 1px 的問題。
	 * （通常是切換多個螢幕開關的時候）
	 * 會做 -1～1 是因為不確定抓標準圖的時候有沒有偏移，乾脆就都掃一遍就算了。
	 */
	private static boolean comparePlus(BufferedImage base, Rect rect) {
		for (int i = -1; i < 2; i++) {
			boolean result = Util.compare(
				base,
				slave.screenShot(
					new Rect(new XY(rect.location.x + i, rect.location.y), rect.size)
				)
			);
			if (result) { return true; }
		}

		return false;
	}
}

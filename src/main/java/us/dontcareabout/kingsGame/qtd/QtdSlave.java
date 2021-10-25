package us.dontcareabout.kingsGame.qtd;

import us.dontcareabout.kingsGame.common.Slave;
import us.dontcareabout.kingsGame.common.XY;

/**
 * 座標、狀態、以及操作行為集散地。
 */
public class QtdSlave {
	private static final Slave slave = Slave.call();
	private static final int rebirthBloodColor = -6547416;

	/** 在基本畫面中，點下去不會造成任何引想的點 */
	public static final XY safePoint = new XY(300, 300);

	private static final XY ascendButton = new XY(100, 340);
	private static final XY ascendConfirm = new XY(335, 495);
	private static final XY ascendJoinConfirm = new XY(70, 495);
	private static final XY ascendOffSeasonEnd = new XY(450, 400);
	private static final XY ascendOnSeasonEnd = new XY(450, 450);
	private static final XY ascendBlood = new XY(80, 320);

	/** {@link Slave#sleep(int)} */
	public static void sleep(int second) {
		slave.sleep(second);
	}

	public static void doAscend() {
		slave.click(ascendButton);
		sleep(5);
		slave.click(isJoinAscend() ? ascendJoinConfirm : ascendConfirm);
		sleep(5);
		slave.click(isOffSeason() ? ascendOffSeasonEnd : ascendOnSeasonEnd);
	}

	public static boolean isOffSeason() {
		return true;	//FIXME
	}

	public static boolean isJoinAscend() {
		return slave.getColor(ascendBlood).getRGB() == rebirthBloodColor;
	}
}

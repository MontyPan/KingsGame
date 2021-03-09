package us.dontcareabout.kingsGame.qtd;

import java.awt.image.BufferedImage;

import javax.swing.JOptionPane;

import us.dontcareabout.kingsGame.common.Rect;
import us.dontcareabout.kingsGame.common.Slave;
import us.dontcareabout.kingsGame.common.Util;
import us.dontcareabout.kingsGame.common.XY;

//Ver 0.0.1
public class QTD {
	private static final int crewWidth = 140;
	private static final int updateEnable = -4444444;
	private static final int rebirthBloodColor = -6547416;

	private static final XY rebirth = new XY(100, 340);
	private static final XY rebirthConfirm = new XY(335, 495);
	private static final XY rebirthJoinConfirm = new XY(70, 495);
	private static final XY rebirthEnd = new XY(450, 400);
	private static final XY rebirthBlood = new XY(80, 320);
	private static final XY crew1 = new XY(25, 520);
	private static final Rect levelArea = new Rect(440, 55, 35, 15);

	private final Slave slave;
	private final Setting setting = new Setting();

	private int[] updateIndexOrder = setting.upgradeOrder();
	private final long levelInterval = setting.levelInterval() * 1000;
	private final long upgradeInterval = setting.upgradeInterval() * 1000;

	private boolean observeMode;
	private long levelCheckTime = Util.now();
	private long upgradeCheckTime = Util.now();

	private BufferedImage preLvImg;

	private QTD() throws Exception {
		slave = new Slave();
	}

	public void setMode(boolean isObserveMode) {
		observeMode = isObserveMode;
	}

	public void start() {
		Util.log("觀察模式：" + observeMode);
		preLvImg = slave.screenShot(levelArea);

		while(true) {
			slave.sleep(1);

			if (Util.now() - upgradeCheckTime > upgradeInterval) {
				upgradeProcess();
			}

			if (Util.now() - levelCheckTime > levelInterval) {
				levelCompareProcess();
			}
		}
	}

	private void upgradeProcess() {
		if (observeMode) { return; }

		for (int i : updateIndexOrder) {
			XY crewXY = getCrewXY(i);
			while (isCanUpgrade(crewXY)) {
				slave.click(crewXY);
				slave.sleep(1);
			}
		}

		upgradeCheckTime = Util.now();
	}

	private void levelCompareProcess() {
		levelCheckTime = Util.now();

		BufferedImage nowLvImg = slave.screenShot(levelArea);
		boolean result = Util.compare(preLvImg, nowLvImg);

		if (!result) {
			preLvImg = nowLvImg;
			return;
		}

		if (observeMode) {
			JOptionPane.showMessageDialog(null, "卡關了，大佬？");
		} else {
			Util.log("重生啦～～～");
			doRebirth();
		}
	}

	private XY getCrewXY(int index) {
		return new XY(crew1.x + index * crewWidth, crew1.y);
	}

	private boolean isCanUpgrade(XY xy) {
		//TODO 改為色系近似判斷
		return slave.getColor(xy).getRGB() > updateEnable;
	}

	private boolean isJoinRebirth() {
		return slave.getColor(rebirthBlood).getRGB() == rebirthBloodColor;
	}

	private void doRebirth() {
		slave.click(rebirth);
		slave.sleep(5);
		slave.click(isJoinRebirth() ? rebirthJoinConfirm : rebirthConfirm);
		slave.sleep(5);
		slave.click(rebirthEnd);
	}

	public static void main(String[] args) throws Exception {
		int type = JOptionPane.showConfirmDialog(null, "觀察模式？", "", JOptionPane.YES_NO_OPTION);
		QTD slave = new QTD();
		slave.setMode(type == 0);
		slave.start();
	}
}

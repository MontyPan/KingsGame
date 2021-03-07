package us.dontcareabout.kingsGame.qtd;

import java.awt.image.BufferedImage;

import us.dontcareabout.kingsGame.common.Rect;
import us.dontcareabout.kingsGame.common.Slave;
import us.dontcareabout.kingsGame.common.Util;
import us.dontcareabout.kingsGame.common.XY;

//Ver 0.0.1
public class QTD {
	private static final long lvCheckSlice = 180 * 1000;
	private static final long upgradeSlice = 30 * 1000;

	private static final int crewWidth = 140;
	private static final int updateEnable = -4444444;
	private static final int updateDisable = -8684165;

	private static final int[] updateIndexOrder = {2, 0, 3, 4, 6, 1, 5};

	private static final XY rebirth = new XY(100, 340);
	private static final XY rebirthConfirm = new XY(335, 495);
	private static final XY rebirthEnd = new XY(450, 400);
	private static final XY crew1 = new XY(25, 520);
	private static final Rect levelArea = new Rect(440, 55, 35, 15);

	private final Slave slave;

	private long levelCheckTime = Util.now();
	private long upgradeTime = Util.now();

	private BufferedImage preLvImg;

	private QTD() throws Exception {
		slave = new Slave();

		preLvImg = slave.screenShot(levelArea);

		while(true) {
			slave.sleep(1);

			if (Util.now() - upgradeTime > upgradeSlice) {
				log("升級時間！ ");	//Delete
				for (int i : updateIndexOrder) {
					XY crewXY = getCrewXY(i);
					while (isCanUpgrade(crewXY)) {
						slave.click(crewXY);
						slave.sleep(1);
					}
				}

				upgradeTime = Util.now();
			}

			if (Util.now() - levelCheckTime > lvCheckSlice) {
				BufferedImage nowLvImg = slave.screenShot(levelArea);
				boolean result = Util.compare(preLvImg, nowLvImg);
				log("重生檢查！ " + result);	//Delete
				if (!result) {
					preLvImg = nowLvImg;
					levelCheckTime = Util.now();
				} else {
					log("重生啦～～～～ ");	//Delete
					doRebirth();
				}
			}
		}
	}

	private XY getCrewXY(int index) {
		return new XY(crew1.x + index * crewWidth, crew1.y);
	}

	private boolean isCanUpgrade(XY xy) {
		//TODO 改為色系近似判斷
		return slave.getColor(xy).getRGB() > updateEnable;
	}

	private void doRebirth() {
		slave.click(rebirth);
		slave.sleep(5);
		slave.click(rebirthConfirm);
		slave.sleep(5);
		slave.click(rebirthEnd);
	}

	private void log(String message) {
		System.out.print(Util.nowText() + " : ");
		System.out.println(message);
	}

	@SuppressWarnings("unused")
	public static void main(String[] args) throws Exception {
		new QTD();
	}
}

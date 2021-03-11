package us.dontcareabout.kingsGame.qtd;

import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import us.dontcareabout.kingsGame.common.Rect;
import us.dontcareabout.kingsGame.common.Slave;
import us.dontcareabout.kingsGame.common.Task;
import us.dontcareabout.kingsGame.common.Util;
import us.dontcareabout.kingsGame.common.XY;

//Ver 0.0.1
public class QTD {
	private static final int crewWidth = 140;
	private static final int updateEnable = -4444444;
	private static final int rebirthBloodColor = -6547416;
	private static final int diamondAdColor = -5385324;

	//廣告大概是 30sec，加上一些操作，保險一點算 60sec
	private static final long adInterval = 60000;

	protected static final XY safeClick = new XY(300, 300);
	private static final XY rebirth = new XY(100, 340);
	private static final XY rebirthConfirm = new XY(335, 495);
	private static final XY rebirthJoinConfirm = new XY(70, 495);
	private static final XY rebirthEnd = new XY(450, 400);
	private static final XY rebirthBlood = new XY(80, 320);
	private static final XY crew1 = new XY(25, 520);
	private static final XY diamondAd = new XY(770, 290);
	private static final Rect levelArea = new Rect(440, 55, 35, 15);
	private static final Rect buyArea = new Rect(235, 105, 40, 20);

	private final Slave slave;
	private final BufferedImage speedBtnImg;

	private final Setting setting = new Setting();
	private final ArrayList<Task> taskList = new ArrayList<>();

	private int[] updateIndexOrder = setting.upgradeOrder();
	private boolean observeMode;

	private Task speedingTask = new Task(adInterval) {
		@Override
		protected void process() {
			if (!isCanSpeeding()) { return; }

			//因為實際操作是交給腳本處理，slave 本身沒啥停頓
			//所以一旦點廣告就得讓其他的 check time 往後延
			diamondTask.delay(adInterval);
			upgradeTask.delay(adInterval);
			levelCompareTask.delay(adInterval);

			//每次加速維持 15 分鐘，所以下次執行期間往後延
			//做這個只是比較有效率、不用一直做 isCanSpeeding() 而已 XD
			this.delay(adInterval * 16);

			doMacro(KeyEvent.VK_P);
		}
	};
	private Task diamondTask = new Task(adInterval * 3) {
		@Override
		protected void process() {
			if (!hasDiamondAD()) { return; }

			//因為實際操作是交給腳本處理，slave 本身沒啥停頓
			//所以一旦點廣告就得讓其他的 check time 往後延
			speedingTask.delay(adInterval);
			upgradeTask.delay(adInterval);
			levelCompareTask.delay(adInterval);

			doMacro(KeyEvent.VK_D);
		}
	};
	private Task upgradeTask = new Task(setting.upgradeInterval() * 1000) {
		@Override
		protected void process() {
			if (observeMode) { return; }

			for (int i : updateIndexOrder) {
				XY crewXY = getCrewXY(i);
				while (isCanUpgrade(crewXY)) {
					slave.click(crewXY);
					slave.sleep(1);
				}
			}
		}
	};
	private Task levelCompareTask = new Task(setting.levelInterval() * 1000) {
		private BufferedImage preLvImg;

		@Override
		protected void process() {
			if (preLvImg == null) {
				preLvImg = slave.screenShot(levelArea);
				return;
			}

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
	};

	private QTD() throws Exception {
		slave = new Slave();
		taskList.add(speedingTask);
		taskList.add(diamondTask);
		taskList.add(upgradeTask);
		taskList.add(levelCompareTask);

		speedBtnImg = ImageIO.read(this.getClass().getClassLoader().getResourceAsStream("QTD/buy.png"));
	}

	public void setMode(boolean isObserveMode) {
		observeMode = isObserveMode;
	}

	public void start() {
		Util.log("觀察模式：" + observeMode);

		while(true) {
			slave.sleep(1);

			for (Task task : taskList) {
				task.check();
			}
		}
	}

	private XY getCrewXY(int index) {
		return new XY(crew1.x + index * crewWidth, crew1.y);
	}

	private boolean isCanSpeeding() {
		return Util.compare(speedBtnImg, slave.screenShot(buyArea));
	}

	private boolean isCanUpgrade(XY xy) {
		//TODO 改為色系近似判斷
		return slave.getColor(xy).getRGB() > updateEnable;
	}

	private boolean isJoinRebirth() {
		return slave.getColor(rebirthBlood).getRGB() == rebirthBloodColor;
	}

	private boolean hasDiamondAD() {
		return slave.getColor(diamondAd).getRGB() == diamondAdColor;
	}

	private void doRebirth() {
		slave.click(rebirth);
		slave.sleep(5);
		slave.click(isJoinRebirth() ? rebirthJoinConfirm : rebirthConfirm);
		slave.sleep(5);
		slave.click(rebirthEnd);
	}

	private void doMacro(int key) {
		slave.click(safeClick);	//隨便點個空地確保是 active window
		slave.sleep(1);
		slave.keyin(KeyEvent.VK_CONTROL, KeyEvent.VK_ALT, key);
	}

	public static void main(String[] args) throws Exception {
		int type = JOptionPane.showConfirmDialog(null, "觀察模式？", "", JOptionPane.YES_NO_OPTION);
		QTD slave = new QTD();
		slave.setMode(type == 0);
		slave.start();
	}
}

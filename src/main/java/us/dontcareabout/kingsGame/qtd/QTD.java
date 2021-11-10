package us.dontcareabout.kingsGame.qtd;

import us.dontcareabout.kingsGame.common.Rect;
import us.dontcareabout.kingsGame.common.Task;
import us.dontcareabout.kingsGame.common.TaskManager;
import us.dontcareabout.kingsGame.common.Util;

public class QTD {
	private static final Rect buyArea = new Rect(235, 105, 40, 20);

	private final Setting setting = new Setting();

	private int team1 = 10;

	private class UpgradeTask extends Task {
		UpgradeTask() {
			super("Upgrade", 0);
			setInterval(setting.upgradeInterval());
		}

		@Override
		protected void process() {
			for (int i : QtdSlave.getUpgradeIndex()) {
				int count = 0;

				while(count < 10 && QtdSlave.upgradeCrew(i)) {
					QtdSlave.sleep(1);
					count++;
				}
			}

			if (QtdSlave.getTeam() != 1) { return; }

			team1--;

			if (team1 == 0) {
				Util.log("切換 team2");
				QtdSlave.swapTeam(2);
				QtdSlave.sleep(2);
			}
		}
	};
	private class StageCompareTask extends Task {
		StageCompareTask() {
			super("Stage Compare", 1);
			setInterval(setting.levelInterval());
		}

		@Override
		protected void process() {
			QtdSlave.compareStage();
			if (QtdSlave.isStageDifferent()) { return; }

			int lvX = QtdSlave.getLvX();
			if (lvX != 0) {
				QtdSlave.swapLvX(lvX - 1);
				return;
			}

			Util.log("重生啦～～～");
			QtdSlave.doAscend();
			QtdSlave.sleep(3);
			QtdSlave.swapTeam(1);
			QtdSlave.sleep(3);
			QtdSlave.swapLvX(2);
			team1 = 15;
		}
	};

	private TaskManager tm = new TaskManager();

	QTD() throws Exception {
		QtdSlave.swapLvX(2);
		tm.add(new UpgradeTask());
		tm.add(new StageCompareTask());
	}

	void start() {
		tm.start();
	}

	public static void main(String[] args) throws Exception {
		QTD slave = new QTD();
		slave.start();
	}
}

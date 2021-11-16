package us.dontcareabout.kingsGame.qtd;

import static us.dontcareabout.kingsGame.qtd.QtdSlave.state;

import us.dontcareabout.kingsGame.common.Rect;
import us.dontcareabout.kingsGame.common.Task;
import us.dontcareabout.kingsGame.common.TaskManager;
import us.dontcareabout.kingsGame.common.Util;

public class QTD {
	private static final Rect buyArea = new Rect(235, 105, 40, 20);

	private class UpgradeTask extends Task {
		UpgradeTask() {
			super("Upgrade", 0);
			setInterval(90);
		}

		@Override
		protected void process() {
			for (int i : state.getUpgradeIndex()) {
				int count = 0;

				while(count < 10 && QtdSlave.upgradeCrew(i)) {
					QtdSlave.sleep(1);
					count++;
				}
			}
		}
	};
	private class StageCompareTask extends Task {
		StageCompareTask() {
			super("Stage Compare", 1);
			setInterval(180);
		}

		@Override
		protected void process() {
			QtdSlave.compareStage();
			if (state.isStageDifferent()) { return; }

			if (state.getTeam() == 1) {
				swap(2);
				return;
			}

			if (state.getTeam() == 2) {
				int lvX = state.getLvX();
				if (lvX != 0) {
					QtdSlave.swapLvX(lvX - 1);
					return;
				} else {
					swap(3);
					return;
				}
			}

			//team = 3
			int lvX = state.getLvX();
			if (lvX != 0) {
				QtdSlave.swapLvX(lvX - 1);
				return;
			}

			Util.log("重生啦～～～");
			QtdSlave.doAscend();
			QtdSlave.sleep(3);
			swap(1);
		}

		private void swap(int team) {
			Util.log("切換 team " + team);
			QtdSlave.swapTeam(team);
			QtdSlave.sleep(2);
			QtdSlave.swapLvX(2);
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

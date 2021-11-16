package us.dontcareabout.kingsGame.qtd.task;

import static us.dontcareabout.kingsGame.qtd.QtdSlave.state;

import us.dontcareabout.kingsGame.common.Task;
import us.dontcareabout.kingsGame.common.Util;
import us.dontcareabout.kingsGame.qtd.QtdSlave;

public class StageCompare extends Task {
	public StageCompare() {
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
}

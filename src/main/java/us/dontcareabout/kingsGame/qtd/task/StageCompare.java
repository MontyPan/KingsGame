package us.dontcareabout.kingsGame.qtd.task;

import static us.dontcareabout.kingsGame.qtd.QtdSlave.state;

import us.dontcareabout.kingsGame.common.OCR.Lang;
import us.dontcareabout.kingsGame.common.Task;
import us.dontcareabout.kingsGame.common.Util;
import us.dontcareabout.kingsGame.qtd.QtdSlave;
import us.dontcareabout.kingsGame.server.Logger;

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
			swap(3);
			return;
		}

		QtdSlave.doAscend();
		QtdSlave.sleep(5);
		swap(1);
	}

	private void swap(int team) {
		Logger.log("切換 team " + team + "(" + Util.ocr(state.getPreStageImage(), Lang.en) + ")");
		QtdSlave.swapTeam(team);
		QtdSlave.sleep(2);
		QtdSlave.swapLvX(2);
	}
}

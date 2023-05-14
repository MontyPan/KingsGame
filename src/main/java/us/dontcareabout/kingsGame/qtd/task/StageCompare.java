package us.dontcareabout.kingsGame.qtd.task;

import static us.dontcareabout.kingsGame.qtd.QtdSlave.state;

import us.dontcareabout.kingsGame.common.OCR.Lang;
import us.dontcareabout.kingsGame.common.Slave;
import us.dontcareabout.kingsGame.common.Task;
import us.dontcareabout.kingsGame.common.Util;
import us.dontcareabout.kingsGame.qtd.QtdCoord;
import us.dontcareabout.kingsGame.qtd.QtdSlave;
import us.dontcareabout.kingsGame.server.Logger;

public class StageCompare extends Task {
	//對，OCR 結果是長這樣... [翻白眼]
	//反正每次結果都一樣就好...
	private static String MY_GAME_OCR = "我 的 諸 戲";

	public StageCompare() {
		super("Stage Compare", 1);
		setInterval(180);
	}

	@Override
	protected void process() {
		QtdSlave.compareStage();
		if (state.isStageDifferent()) { return; }

		//遊戲有機率自動跳回 BS 主頁面
		//stage 的 OCR 結果都會是空白
		//所以在切換 team 之前先判斷
		//方法是判斷指定區域的文字是不是「我的遊戲」
		if (MY_GAME_OCR.equals(Util.ocr(Slave.call().screenShot(QtdCoord.bsMyGame), Lang.zh_tw))) {
			Logger.log("跳回 BS，嘗試重新進入 QTD");
			Slave.call().click(QtdCoord.bsQtdIcon);
			QtdSlave.enterQTD();
			QtdSlave.swapLvX(state.getLvX());
			//預防切換 team 的動作沒有被記錄到，所以無差別重新指定一次
			swap(QtdSlave.state.getTeam());
			return;
		}

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
		Logger.log("切換 team " + team + " (" + state.getPreStage() + ")");
		QtdSlave.swapTeam(team);
		QtdSlave.sleep(2);
		QtdSlave.swapLvX(2);
	}
}

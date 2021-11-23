package us.dontcareabout.kingsGame.qtd;

import us.dontcareabout.kingsGame.common.Rect;
import us.dontcareabout.kingsGame.common.TaskManager;
import us.dontcareabout.kingsGame.qtd.task.StageCompare;
import us.dontcareabout.kingsGame.qtd.task.Upgrade;

public class QTD extends TaskManager {
	private static final Rect buyArea = new Rect(235, 105, 40, 20);

	public QTD() {
		QtdSlave.swapLvX(2);
		add(new Upgrade());
		add(new StageCompare());
	}

	public static void main(String[] args) throws Exception {
		QTD slave = new QTD();
		slave.start();
	}
}

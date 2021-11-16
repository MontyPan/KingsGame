package us.dontcareabout.kingsGame.qtd;

import us.dontcareabout.kingsGame.common.Rect;
import us.dontcareabout.kingsGame.common.TaskManager;
import us.dontcareabout.kingsGame.qtd.task.StageCompare;
import us.dontcareabout.kingsGame.qtd.task.Upgrade;

public class QTD {
	private static final Rect buyArea = new Rect(235, 105, 40, 20);

	private TaskManager tm = new TaskManager();

	QTD() throws Exception {
		QtdSlave.swapLvX(2);
		tm.add(new Upgrade());
		tm.add(new StageCompare());
	}

	void start() {
		tm.start();
	}

	public static void main(String[] args) throws Exception {
		QTD slave = new QTD();
		slave.start();
	}
}

package us.dontcareabout.kingsGame.qtd.task;

import static us.dontcareabout.kingsGame.qtd.QtdSlave.state;

import us.dontcareabout.kingsGame.common.Task;
import us.dontcareabout.kingsGame.qtd.QtdSlave;

public class Upgrade extends Task {
	public Upgrade() {
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
}

package us.dontcareabout.kingsGame.qtd;

import us.dontcareabout.kingsGame.common.DoubleProperties;

public class Setting extends DoubleProperties {
	private static final int[] defaultOrder = {0, 1, 2, 3, 4, 5, 6};

	public Setting() {
		super("QTD.xml", "QTD.xml");
	}

	public int[] upgradeOrder() {
		String[] values = getProperty("upgradeOrder").split(",");

		try {
			int[] result = new int[7];

			for (int i = 0; i < result.length; i++) {
				int v = Integer.parseInt(values[i].trim());
				result[i] = v > result.length ? 0 : v;
			}

			return result;
		} catch (Exception e) {
			return defaultOrder;
		}
	}

	public int levelInterval() {
		try {
			return Integer.parseInt(getProperty("levelInterval"));
		} catch (Exception e) {
			return 300;
		}
	}

	public int upgradeInterval() {
		try {
			return Integer.parseInt(getProperty("upgradeInterval"));
		} catch (Exception e) {
			return 60;
		}
	}
}

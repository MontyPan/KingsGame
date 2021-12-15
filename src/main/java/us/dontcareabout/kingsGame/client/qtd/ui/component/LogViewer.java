package us.dontcareabout.kingsGame.client.qtd.ui.component;

import java.util.List;

import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HorizontalLayoutContainer.HorizontalLayoutData;
import com.sencha.gxt.widget.core.client.container.VerticalLayoutContainer;
import com.sencha.gxt.widget.core.client.form.TextArea;

import us.dontcareabout.kingsGame.client.Util;
import us.dontcareabout.kingsGame.client.qtd.data.DataCenter;
import us.dontcareabout.kingsGame.shared.Log;
import us.dontcareabout.kingsGame.shared.qtd.Action;

public class LogViewer extends VerticalLayoutContainer {
	private static final Margins margins = new Margins(2, 5, 2, 5);
	private static final HorizontalLayoutData btnHLD = new HorizontalLayoutData(0.5, 1, new Margins(0, 2, 0, 2));

	private TextArea logTA = new TextArea();
	private TextButton refreshBtn = new TextButton("更新");
	private TextButton cleanBtn = new TextButton("清空");

	public LogViewer() {
		refreshBtn.addSelectHandler(e -> DataCenter.getLog());
		cleanBtn.addSelectHandler(e -> DataCenter.action(Action.clearLog));

		HorizontalLayoutContainer btnHLC = new HorizontalLayoutContainer();
		btnHLC.add(refreshBtn, btnHLD);
		btnHLC.add(cleanBtn, btnHLD);

		add(logTA, new VerticalLayoutData(1, 1, margins));
		add(btnHLC, new VerticalLayoutData(1, 34, margins));

		DataCenter.addLogReady(e -> refresh(e.data));
	}

	private void refresh(List<Log> logList) {
		StringBuffer result = new StringBuffer();

		for (Log log : logList) {
			result.append(Util.toString(log.getTime()) + " : " + log.getMessage() + "\n");
		}

		logTA.setText(result.toString());
	}
}

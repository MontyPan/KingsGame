package us.dontcareabout.kingsGame.client.qtd.ui.component;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.SimpleBeanEditorDriver;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.widget.core.client.Composite;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.form.IntegerField;

import us.dontcareabout.kingsGame.client.qtd.data.DataCenter;
import us.dontcareabout.kingsGame.shared.qtd.ShotRect;

public class ScreenShooter extends Composite implements Editor<ShotRect> {
	private static ScreenShooterUiBinder uiBinder = GWT.create(ScreenShooterUiBinder.class);
	private static Driver driver = GWT.create(Driver.class);

	@UiField IntegerField x;
	@UiField IntegerField y;
	@UiField IntegerField w;
	@UiField IntegerField h;
	@UiField @Ignore Image shot;

	public ScreenShooter() {
		initWidget(uiBinder.createAndBindUi(this));
		driver.initialize(this);
		driver.edit(new ShotRect());
		DataCenter.addShootScreenFinish(e -> shot.setUrl(e.data));
	}

	@UiHandler("shoot")
	void clickShoot(SelectEvent se) {
		DataCenter.shootScreen(driver.flush());
	}

	interface ScreenShooterUiBinder extends UiBinder<Widget, ScreenShooter> {}
	interface Driver extends SimpleBeanEditorDriver<ShotRect, ScreenShooter> {}
}

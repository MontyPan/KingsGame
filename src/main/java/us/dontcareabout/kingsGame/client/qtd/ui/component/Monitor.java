package us.dontcareabout.kingsGame.client.qtd.ui.component;

import com.sencha.gxt.chart.client.draw.RGB;
import com.sencha.gxt.chart.client.draw.sprite.SpriteSelectionEvent.SpriteSelectionHandler;
import com.sencha.gxt.core.client.util.Margins;

import us.dontcareabout.gwt.client.Console;
import us.dontcareabout.gwt.client.util.ImageUtil;
import us.dontcareabout.gxt.client.draw.LImageSprite;
import us.dontcareabout.gxt.client.draw.LayerContainer;
import us.dontcareabout.gxt.client.draw.component.TextButton;
import us.dontcareabout.gxt.client.draw.layout.HorizontalLayoutLayer;
import us.dontcareabout.gxt.client.draw.layout.VerticalLayoutLayer;
import us.dontcareabout.kingsGame.client.gf.CenterLayoutLayer;
import us.dontcareabout.kingsGame.client.qtd.data.DataCenter;
import us.dontcareabout.kingsGame.shared.qtd.Parameter;

public class Monitor extends LayerContainer {
	private static final int iconSize = 30;

	private VerticalLayoutLayer root = new VerticalLayoutLayer();
	private LImageSprite screen = new LImageSprite();
	private LImageSprite stage = new LImageSprite();
	private UpgradeBtn[] upgradeBtns = new UpgradeBtn[7];	//Refactory magic number

	public Monitor() {
		Toolbar toolbarL = new Toolbar();
		toolbarL.addIcon("重", e -> Console.log("重"));	//FIXME
		toolbarL.addIcon("停", e -> Console.log("停"));

		Toolbar toolbarR = new Toolbar();
		toolbarR.addIcon("刷", e -> DataCenter.getState());

		HorizontalLayoutLayer upBar = new HorizontalLayoutLayer();
		upBar.setMargins(2);
		upBar.setGap(2);
		upBar.setBgColor(RGB.LIGHTGRAY);
		upBar.addChild(toolbarL, toolbarL.getIconAmount() * (iconSize + 2) + 2);
		upBar.addChild(new CenterLayoutLayer(stage, Parameter.STAGE_WIDTH, Parameter.STAGE_HEIGHT), 1);
		upBar.addChild(toolbarR, toolbarR.getIconAmount() * (iconSize + 2) + 2);

		HorizontalLayoutLayer upgradeHLL = new HorizontalLayoutLayer();
		upgradeHLL.setMargins(new Margins(0, 2, 2, 2));
		upgradeHLL.setGap(3);

		for (int i = 0; i < upgradeBtns.length; i++) {
			upgradeBtns[i] = new UpgradeBtn(i);
			upgradeHLL.addChild(upgradeBtns[i], 1.0 / upgradeBtns.length);
		}

		root.addChild(upBar, iconSize + 4);
		root.addChild(
			new CenterLayoutLayer(screen, Parameter.SCREEN_WIDTH, Parameter.SCREEN_HEIGHT),
			1
		);
		root.addChild(upgradeHLL, 30);
		addLayer(root);

		DataCenter.addStateReady(
			event -> {
				screen.setResource(ImageUtil.toResource(event.data.getScreenImage()));
				stage.setResource(ImageUtil.toResource(event.data.getStageImage()));

				for (UpgradeBtn btn : upgradeBtns) {
					btn.on = false;
					for (int index : event.data.getUpgradeIndex()) {
						if (btn.index != index) { continue; }

						btn.on = true;
						break;
					}
					btn.refresh();
				}

				//XXX ImageResource 的 size 暫時無解，先撐著用
				adjustMember(getOffsetWidth(), getOffsetHeight());

				redrawSurface();
			}
		);
	}

	@Override
	protected void adjustMember(int width, int height) {
		root.resize(width, height);
	}

	@Override
	protected void onLoad() {
		super.onLoad();
		DataCenter.getState();
	}

	class Toolbar extends HorizontalLayoutLayer {
		Toolbar() {
			setGap(2);
			setMargins(new Margins(0, 2, 0, 2));
		}

		void addIcon(String word, SpriteSelectionHandler h) {
			TextButton icon = new TextButton(word);
			icon.setBgRadius(5);
			icon.setBgColor(RGB.BLACK);
			icon.setTextColor(RGB.WHITE);
			icon.addSpriteSelectionHandler(h);
			addChild(icon, iconSize + 2);
		}

		int getIconAmount() {
			return this.getMembers().size() - 1;
		}
	}

	class UpgradeBtn extends TextButton {
		final int index;
		boolean on;

		public UpgradeBtn(int i) {
			index = i;
			setText("" + i);
			setTextColor(RGB.WHITE);
			setBgRadius(5);
			refresh();
		}

		private void refresh() {
			setBgColor(on ? RGB.RED : RGB.LIGHTGRAY);
		}
	}
}

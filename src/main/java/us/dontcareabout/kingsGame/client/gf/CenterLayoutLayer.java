package us.dontcareabout.kingsGame.client.gf;

import us.dontcareabout.gxt.client.draw.LImageSprite;
import us.dontcareabout.gxt.client.draw.LayerSprite;

/**
 * 只有一個固定大小的 sprite，如果本身 size 大於 sprite，會將其置中。
 * <p>
 * 由於 GXT「sprite remove 之後就無法再加回去」的特性，所以不提供切換 sprite 的功能。
 */
//TODO GF 增加一個 HasSize interface，然後把傳入型態改成 HasSize
//XXX 可以考慮改成給 margin，然後變成相對泛用的 layout？
public class CenterLayoutLayer extends LayerSprite {
	private final LImageSprite sprite;

	private double fixWidth;
	private double fixHeight;

	public CenterLayoutLayer(LImageSprite sprite, double fixWidth, double fixHeight) {
		this.sprite = sprite;
		this.fixWidth = fixWidth;
		this.fixHeight = fixHeight;
		add(sprite);
	}

	public double getFixWidth() {
		return fixWidth;
	}

	public void setFixWidth(double fixWidth) {
		this.fixWidth = fixWidth;
		adjustMember();
	}

	public double getFixHeight() {
		return fixHeight;
	}

	public void setFixHeight(double fixHeight) {
		this.fixHeight = fixHeight;
		adjustMember();
	}

	@Override
	protected void adjustMember() {
		sprite.setWidth(fixWidth);
		sprite.setHeight(fixHeight);
		sprite.setLX(getWidth() > fixWidth ? (getWidth() - fixWidth) / 2 : 0);
		sprite.setLY(getHeight() > fixHeight ? (getHeight() - fixHeight) / 2 : 0);
	}
}

package us.dontcareabout.kingsGame.shared.qtd;

import us.dontcareabout.kingsGame.common.Rect;

/**
 * 其實跟 {@link Rect} 完全一樣，
 * 但是 {@link Rect} 當初沒放在 shared 底下、GWT 過不了，
 * 然後又設計成 immutable，server side deserialize 應該會有問題（沒測）
 * 就... 算啦 XD
 */
public class ShotRect {
	private int x;
	private int y;
	private int w;
	private int h;
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getW() {
		return w;
	}
	public void setW(int w) {
		this.w = w;
	}
	public int getH() {
		return h;
	}
	public void setH(int h) {
		this.h = h;
	}
	//Delete
	@Override
	public String toString() {
		return "ShotRect [x=" + x + ", y=" + y + ", w=" + w + ", h=" + h + "]";
	}
}

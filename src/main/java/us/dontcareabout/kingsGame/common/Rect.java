package us.dontcareabout.kingsGame.common;

public class Rect {
	public final XY location;
	public final XY size;

	public Rect(int x, int y, int w, int h) {
		this(new XY(x, y), new XY(w, h));
	}

	public Rect(XY location, XY size) {
		this.location = location;
		this.size = size;
	}
}

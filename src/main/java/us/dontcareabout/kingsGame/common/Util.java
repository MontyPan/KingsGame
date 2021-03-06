package us.dontcareabout.kingsGame.common;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

	public static void log(String message) {
		System.out.print(Util.nowText() + " : ");
		System.out.println(message);
	}

	public static String nowText() { return dateFormat.format(new Date()); }

	public static long now() { return new Date().getTime(); }

	public static double colorDiff(Color a, Color b) {
		return Math.sqrt(
			2 * Math.pow(a.getRed() - b.getRed(), 2) +
			4 * Math.pow(a.getGreen() - b.getGreen(), 2) +
			3 * Math.pow(a.getBlue() - b.getBlue(), 2)
		);
	}

	public static boolean compare(BufferedImage a, BufferedImage b) {
		if (a.getWidth() != b.getWidth()) { return false; }
		if (a.getHeight() != b.getHeight()) { return false; }

		for (int x = 0; x < a.getWidth(); x++) {
			for (int y = 0; y < a.getHeight(); y++) {
				if (a.getRGB(x, y) != b.getRGB(x, y)) { return false; }
			}
		}

		return true;
	}
}

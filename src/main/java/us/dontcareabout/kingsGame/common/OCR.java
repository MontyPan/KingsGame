package us.dontcareabout.kingsGame.common;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.stream.Collectors;

import net.sourceforge.tess4j.Tesseract;

/**
 * {@link Tesseract} wrapper
 */
public class OCR {
	private Tesseract instance = new Tesseract();

	public OCR(String datapath) {
		instance.setDatapath(datapath);
	}

	public void setLanguage(Lang... langs) {
		if (langs == null || langs.length == 0) {
			langs = Lang.values();
		}
		instance.setLanguage(
			Arrays.asList(langs).stream()
				.map(l -> l.string).collect(Collectors.joining("+"))
		);
	}

	public String parse(BufferedImage image) {
		try {
			return instance.doOCR(image).trim();
		} catch (Exception e) {
			return null;
		}
	}

	public static enum Lang {
		en("eng"),
		zh_cn("chi_sim"),
		zh_tw("chi_tra"),
		;

		public final String string;
		private Lang(String value) {
			this.string = value;
		}
	}
}

package us.dontcareabout.kingsGame.client;

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;

public class Util {
	private static final DateTimeFormat format = DateTimeFormat.getFormat("MM.dd HH:mm:ss");

	public static String toString(Date date) {
		return format.format(date);
	}
}

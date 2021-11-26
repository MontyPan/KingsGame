package us.dontcareabout.kingsGame.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import us.dontcareabout.kingsGame.common.Util;
import us.dontcareabout.kingsGame.shared.Log;

public class Logger {
	private static final List<Log> list = new ArrayList<>();

	public static void log(String message) {
		Util.log(message);
		list.add(new Log(new Date(), message));
	}

	public static List<Log> getList() {
		return Collections.unmodifiableList(list);
	}

	public static void clear() {
		list.clear();
	}
}

package us.dontcareabout.kingsGame.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

//XXX 懶得套 GF，所以直接把 class 幹過來用
public class DoubleProperties {
	private Properties inner = new Properties();
	private Properties outter = new Properties();
	private boolean outterFlag = false;

	public DoubleProperties(String innerName, String outterName) {
		try {
			inner.loadFromXML(getResourceStream(innerName));
		} catch (IOException e) {}

		try {
			InputStream outterIS = getResourceStream(outterName);

			if (outterIS == null) { return; }

			outter.loadFromXML(outterIS);
			outterFlag = true;
		} catch(IOException e) {}
	}

	public DoubleProperties(String innerName, File outterFile) {
		try {
			inner.loadFromXML(getResourceStream(innerName));
		} catch (IOException e) {}

		try {
			if (outterFile == null) { return; }

			outter.loadFromXML(new FileInputStream(outterFile));
			outterFlag = true;
		} catch(IOException e) {}
	}

	protected String getProperty(String key) {
		if (outterFlag && outter.getProperty(key) != null) {
			return outter.getProperty(key);
		} else {
			return inner.getProperty(key);
		}
	}

	protected String getProperty(String key, String defaultValue) {
		if (outterFlag && outter.getProperty(key) != null) {
			return outter.getProperty(key);
		} else {
			return inner.getProperty(key, defaultValue);
		}
	}

	/**
	 * 將指定的 property 以行為單位、過濾空白行、並刪除行首行尾的空白，
	 * 並轉為 String 的 List。
	 */
	protected List<String> getPropertyList(String key) {
		ArrayList<String> result = new ArrayList<>();
		String property = getProperty(key);

		if (property == null) { return result; }

		for (String line : property.split("\n")) {
			String clean = line.trim();

			if (clean.isEmpty()) { continue; }

			result.add(clean);
		}

		return result;
	}

	private InputStream getResourceStream(String name) {
		return this.getClass().getClassLoader().getResourceAsStream(name);
	}
}

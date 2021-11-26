package us.dontcareabout.kingsGame.shared;

import java.util.Date;

public class Log {
	private Date time;
	private String message;

	//for GwtJackson
	Log() {}

	public Log(Date time, String message) {
		this.time = time;
		this.message = message;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
}

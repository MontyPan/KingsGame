package us.dontcareabout.kingsGame.client.qtd.data;

import java.util.List;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import us.dontcareabout.kingsGame.client.qtd.data.LogReadyEvent.LogReadyHandler;
import us.dontcareabout.kingsGame.shared.Log;

public class LogReadyEvent extends GwtEvent<LogReadyHandler> {
	public static final Type<LogReadyHandler> TYPE = new Type<LogReadyHandler>();

	public final List<Log> data;

	public LogReadyEvent(List<Log> data) {
		this.data = data;
	}

	@Override
	public Type<LogReadyHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(LogReadyHandler handler) {
		handler.onLogReady(this);
	}

	public interface LogReadyHandler extends EventHandler{
		public void onLogReady(LogReadyEvent event);
	}
}

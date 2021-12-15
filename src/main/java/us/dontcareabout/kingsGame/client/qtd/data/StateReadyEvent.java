package us.dontcareabout.kingsGame.client.qtd.data;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import us.dontcareabout.kingsGame.client.qtd.data.StateReadyEvent.StateReadyHandler;
import us.dontcareabout.kingsGame.shared.qtd.State;

public class StateReadyEvent extends GwtEvent<StateReadyHandler> {
	public static final Type<StateReadyHandler> TYPE = new Type<StateReadyHandler>();
	public final State data;

	public StateReadyEvent(State data) {
		this.data = data;
	}

	@Override
	public Type<StateReadyHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(StateReadyHandler handler) {
		handler.onStateReady(this);
	}

	public interface StateReadyHandler extends EventHandler{
		public void onStateReady(StateReadyEvent event);
	}
}

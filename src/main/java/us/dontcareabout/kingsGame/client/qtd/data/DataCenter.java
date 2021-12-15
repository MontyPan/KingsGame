package us.dontcareabout.kingsGame.client.qtd.data;

import java.util.List;

import com.github.nmorel.gwtjackson.client.ObjectMapper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.SimpleEventBus;

import us.dontcareabout.kingsGame.client.gf.GetRequest;
import us.dontcareabout.kingsGame.client.qtd.data.StateReadyEvent.StateReadyHandler;
import us.dontcareabout.kingsGame.client.qtd.data.LogReadyEvent.LogReadyHandler;
import us.dontcareabout.kingsGame.shared.Log;
import us.dontcareabout.kingsGame.shared.qtd.Action;
import us.dontcareabout.kingsGame.shared.qtd.State;

public class DataCenter {
	private final static SimpleEventBus eventBus = new SimpleEventBus();

	public interface ActionMapper extends ObjectMapper<Action> {}
	private static ObjectMapper<Action> actionMapper = GWT.create(ActionMapper.class);

	public static void action(Action action) {
		GetRequest<Action> request = new GetRequest<>();
		request.setPath("qtd/action/" + action).setReader(actionMapper)
			.setCallback(data -> getLog())
			.send();
	}

	////////////////

	public interface LogListMapper extends ObjectMapper<List<Log>> {}
	private static ObjectMapper<List<Log>> logListMapper = GWT.create(LogListMapper.class);

	public static void getLog() {
		GetRequest<List<Log>> request = new GetRequest<>();
		request.setPath("qtd/log").setReader(logListMapper)
			.setCallback(
				data -> eventBus.fireEvent(new LogReadyEvent(data))
			).send();
	}

	public static HandlerRegistration addLogReady(LogReadyHandler handler) {
		return eventBus.addHandler(LogReadyEvent.TYPE, handler);
	}

	////////////////

	public interface StateMapper extends ObjectMapper<State> {}
	private static ObjectMapper<State> stateMapper = GWT.create(StateMapper.class);

	public static void getState() {
		GetRequest<State> request = new GetRequest<>();
		request.setPath("qtd/state").setReader(stateMapper)
			.setCallback(
				data -> eventBus.fireEvent(new StateReadyEvent(data))
			).send();
	}

	public static HandlerRegistration addStateReady(StateReadyHandler handler) {
		return eventBus.addHandler(StateReadyEvent.TYPE, handler);
	}
}
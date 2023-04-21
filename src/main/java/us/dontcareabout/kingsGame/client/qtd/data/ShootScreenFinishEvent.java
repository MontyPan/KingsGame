package us.dontcareabout.kingsGame.client.qtd.data;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import us.dontcareabout.kingsGame.client.qtd.data.ShootScreenFinishEvent.ShootScreenFinishHandler;

public class ShootScreenFinishEvent extends GwtEvent<ShootScreenFinishHandler> {
	public static final Type<ShootScreenFinishHandler> TYPE = new Type<ShootScreenFinishHandler>();

	public final String data;

	public ShootScreenFinishEvent(String data) {
		this.data = data;
	}

	@Override
	public Type<ShootScreenFinishHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ShootScreenFinishHandler handler) {
		handler.onShootScreenFinish(this);
	}

	public interface ShootScreenFinishHandler extends EventHandler{
		public void onShootScreenFinish(ShootScreenFinishEvent event);
	}
}

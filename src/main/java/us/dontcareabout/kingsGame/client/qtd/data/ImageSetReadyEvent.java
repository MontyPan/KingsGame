package us.dontcareabout.kingsGame.client.qtd.data;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;

import us.dontcareabout.kingsGame.client.qtd.data.ImageSetReadyEvent.ImageSetReadyHandler;
import us.dontcareabout.kingsGame.shared.qtd.ImageSet;

public class ImageSetReadyEvent extends GwtEvent<ImageSetReadyHandler> {
	public static final Type<ImageSetReadyHandler> TYPE = new Type<ImageSetReadyHandler>();
	public final ImageSet data;

	public ImageSetReadyEvent(ImageSet data) {
		this.data = data;
	}

	@Override
	public Type<ImageSetReadyHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(ImageSetReadyHandler handler) {
		handler.onImageSetReady(this);
	}

	public interface ImageSetReadyHandler extends EventHandler{
		public void onImageSetReady(ImageSetReadyEvent event);
	}
}

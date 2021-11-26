package us.dontcareabout.kingsGame.client.gf;

import com.github.nmorel.gwtjackson.client.ObjectReader;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;

import us.dontcareabout.gwt.client.data.Callback;

public class GetRequest<T> {
	private String path;
	private ObjectReader<T> reader;
	private Callback<T> callback;

	public GetRequest<T> setPath(String path) {
		this.path = path;
		return this;
	}

	public GetRequest<T> setReader(ObjectReader<T> reader) {
		this.reader = reader;
		return this;
	}

	public GetRequest<T> setCallback(Callback<T> callback) {
		this.callback = callback;
		return this;
	}

	public void send() {
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, path);
		try {
			builder.sendRequest(null, new RequestCallback() {
				@Override
				public void onResponseReceived(Request request, Response response) {
					callback.onSuccess(reader.read(response.getText()));
				}

				@Override
				public void onError(Request request, Throwable exception) {
					callback.onError(exception);
				}
			});
		} catch (RequestException exception) {
			callback.onError(exception);
		}
	}
}
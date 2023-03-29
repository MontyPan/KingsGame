package us.dontcareabout.kingsGame.client.gf;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;

import us.dontcareabout.gwt.client.data.Callback;

//Refactory GF
public class PostRequest {
	private String path;
	private Callback<String> callback;

	public PostRequest setPath(String path) {
		this.path = path;
		return this;
	}

	public PostRequest setCallback(Callback<String> callback) {
		this.callback = callback;
		return this;
	}

	public void send(String data) {
		RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, path);
		builder.setHeader("Content-Type", "application/json");

		try {
			builder.sendRequest(data, new RequestCallback() {
				@Override
				public void onResponseReceived(Request request, Response response) {
					callback.onSuccess(response.getText());
				}

				@Override
				public void onError(Request request, Throwable exception) {
					callback.onError(exception);
				}
			});
		} catch(RequestException exception) {
			callback.onError(exception);
		}
	}
}

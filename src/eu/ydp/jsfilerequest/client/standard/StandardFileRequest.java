package eu.ydp.jsfilerequest.client.standard;

import static eu.ydp.jsfilerequest.client.util.Logger.log;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;

import eu.ydp.jsfilerequest.client.AbstractFileRequest;
import eu.ydp.jsfilerequest.client.FileRequest;
import eu.ydp.jsfilerequest.client.FileRequestCallback;
import eu.ydp.jsfilerequest.client.FileRequestException;

public class StandardFileRequest extends AbstractFileRequest {
	
	public void send(String requestData, final FileRequestCallback callback) throws FileRequestException{
		RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, getUrl());
		final FileRequest instance = this;
		try {
			requestBuilder.sendRequest(requestData, new RequestCallback() {
				
				@Override
				public void onResponseReceived(Request request, Response response) {
					log("Standard request response received: " + getUrl());
					callback.onResponseReceived(instance, new StandardFileResponse(response));
				}
				
				@Override
				public void onError(Request request, Throwable exception) {
					log("Standard request response error: " + getUrl());
					callback.onError(instance, exception);
				}
			});
		} catch (Exception e) {
			throw new FileRequestException(e);
		}
		
	}

}

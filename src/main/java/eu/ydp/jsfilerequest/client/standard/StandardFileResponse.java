package eu.ydp.jsfilerequest.client.standard;

import com.google.gwt.http.client.Response;

import eu.ydp.jsfilerequest.client.FileResponse;

public class StandardFileResponse implements FileResponse {

	private Response response;

	public StandardFileResponse(Response response){
		this.response = response;
	}
	
	@Override
	public int getStatusCode() {
		return response.getStatusCode();
	}

	@Override
	public String getStatusText() {
		return response.getStatusText();
	}

	@Override
	public String getText() {
		return response.getText();
	}

}

package eu.ydp.jsfilerequest.client.jsinject;
import eu.ydp.jsfilerequest.client.AbstractFileRequest;
import eu.ydp.jsfilerequest.client.FileRequestCallback;
import eu.ydp.jsfilerequest.client.FileRequestException;

class JsInjectFileRequest extends AbstractFileRequest {
	
	@Override
	public void send(String requestData, FileRequestCallback callback) throws FileRequestException {
		JsInjectRequestPerformer.getInstance().queueRequest(this, callback);
	}

}

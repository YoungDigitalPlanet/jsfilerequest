package eu.ydp.jsfilerequest.client;


public class RequestAction {

	private FileRequest request;
	private FileRequestCallback callback;
	
	public RequestAction(FileRequest request, FileRequestCallback callback){
		this.request = request;
		this.callback = callback;
	}

	public FileRequest getRequest() {
		return request;
	}

	public FileRequestCallback getCallback() {
		return callback;
	}
	
	
	
}

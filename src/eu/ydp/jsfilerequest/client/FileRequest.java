package eu.ydp.jsfilerequest.client;

public interface FileRequest {
	
	public String getUrl();
	public void setUrl(String url);
	
	public void send(String requestData, final FileRequestCallback callback) throws FileRequestException;
}

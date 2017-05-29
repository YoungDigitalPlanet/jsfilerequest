package eu.ydp.jsfilerequest.client;

/**
 * Interface for file request.
 * 
 * @author Rafal Rybacki rrybacki@ydp.com.pl
 *
 */
public interface FileRequest {
	
	public String getUrl();
	
	public void setUrl(String url);
	
	public void send(String requestData, final FileRequestCallback callback) throws FileRequestException;
}

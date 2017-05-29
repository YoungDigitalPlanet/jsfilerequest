package eu.ydp.jsfilerequest.client;

public abstract class AbstractFileRequest implements FileRequest {

	private String url;

	@Override
	public String getUrl() {
		return url;
	}

	@Override
	public void setUrl(String url) {
		this.url = url;
	}	

}

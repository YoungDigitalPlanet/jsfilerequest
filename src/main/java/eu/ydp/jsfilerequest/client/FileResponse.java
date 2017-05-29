package eu.ydp.jsfilerequest.client;

public interface FileResponse {

	  public int getStatusCode();

	  public String getStatusText();
	  
	  public String getText();
}

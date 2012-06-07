package eu.ydp.jsfilerequest.client;

public interface FileRequestCallback {

	  void onResponseReceived(FileRequest request, FileResponse response);

	  void onError(FileRequest request, Throwable exception);
}

package eu.ydp.jsfilerequest.client.jsinject;

import eu.ydp.jsfilerequest.client.FileResponse;

class JsInjectFileResponse implements FileResponse {

	
	private boolean success;
	private String text;

	private final static int CODE_OK = 200;
	private final static int CODE_NOT_FOUND = 404;
	
	private final static String TEXT_OK = "OK";
	private final static String TEXT_NOT_FOUND = "Not found";

	private JsInjectFileResponse(boolean success, String text){
		this.success = success ;
		this.text = text;
	}

	public static JsInjectFileResponse createSuccessResponse(String text){
		return new JsInjectFileResponse(true, text);
	}
	
	public static JsInjectFileResponse createErrorResponse(){
		return new JsInjectFileResponse(false, "");
	}
	
	@Override
	public int getStatusCode() {
		if (success){
			return CODE_OK;
		} 
		return CODE_NOT_FOUND;
	}

	@Override
	public String getStatusText() {
		if (success){
			return TEXT_OK;
		} 
		return TEXT_NOT_FOUND;
	}

	@Override
	public String getText() {
		return text;
	}

}

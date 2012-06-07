package eu.ydp.jsfilerequest.client.util;

public abstract class Logger {

	public static native void log(String msg)/*-{
		if ($wnd.jsFileRequestEnableLogging  &&  typeof console == 'object'){
			console.log(msg);
		}
	}-*/;
}

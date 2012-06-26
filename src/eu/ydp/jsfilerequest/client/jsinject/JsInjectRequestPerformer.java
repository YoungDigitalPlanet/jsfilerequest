package eu.ydp.jsfilerequest.client.jsinject;


import static eu.ydp.jsfilerequest.client.util.Logger.log;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.ScriptInjector;
import com.google.gwt.core.client.ScriptInjector.FromUrl;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.user.client.Timer;

import eu.ydp.jsfilerequest.client.FileRequest;
import eu.ydp.jsfilerequest.client.FileRequestCallback;
import eu.ydp.jsfilerequest.client.RequestAction;
import eu.ydp.jsfilerequest.client.util.SimpleQueue;

/**
 * Class responsible for managing the queue of the files to load,
 * fetching the file contents and running the callback.
 * 
 * @author Rafal Rybacki rrybacki@ydp.com.pl
 *
 */
public class JsInjectRequestPerformer {

	private static JsInjectRequestPerformer instance;
	private int TIMEOUT = 1000; // low timeout as the Request Performer will be user only locally
	private Timer timeoutTimer;
	
	private SimpleQueue<RequestAction> requests = new SimpleQueue<RequestAction>();
	private RequestAction currRequestAction;
	private boolean requestUnderway = false;
	private JavaScriptObject lastScriptElement;

	private JsInjectRequestPerformer(){
		registerGlobalCallback();
		
		timeoutTimer = new Timer() {
			
			@Override
			public void run() {
				onTimeout();
			}
		};
	}
	
	public static JsInjectRequestPerformer getInstance(){
		if (instance == null){
			instance = new JsInjectRequestPerformer();
		}
		return instance;
	}
	
	/**
	 * Queues the subsequent file and starts files fetching process.
	 * 
	 * @param request Request to be queued
	 * @param callback Callback functions.
	 */
	public void queueRequest(FileRequest request, FileRequestCallback callback){		
		requests.offer(new RequestAction(request, callback));
		log("Adding " + request.getUrl() + " to the queue. The queue contains now " + requests.size() + " elements.");
		if (!requestUnderway){
			sendNextRequest();
		}
	}
	
	private synchronized void sendNextRequest(){
		if (lastScriptElement != null){
			removeScriptElement(lastScriptElement);
			lastScriptElement = null;
		}
		if (requests.size() > 0){
			requestUnderway = true;
			currRequestAction = requests.peek();
			String url = requests.peek().getRequest().getUrl() + getFileSuffix();
			log("Sending request for url: " + url);
			injectScriptNodeIntoDom(url);
			timeoutTimer.schedule(TIMEOUT);
		} else {
			requestUnderway = false;
		}
	}
	
	private void injectScriptNodeIntoDom(String scriptUrl){
		final FromUrl scriptFromUrl = ScriptInjector.fromUrl(scriptUrl).setCallback(new Callback<Void, Exception>() {
			
			@Override
			public void onSuccess(Void result) {
			}
			
			@Override
			public void onFailure(Exception reason) {
				onFileFailure(reason);
			}
		});
		scriptFromUrl.setWindow(getTopWindow());
		lastScriptElement = scriptFromUrl.inject();
	}
	
	private native JavaScriptObject getTopWindow()/*-{
		return $wnd;
	}-*/;
	
	private native void removeScriptElement(JavaScriptObject element)/*-{
		var parent = element.parentNode;
		if (parent != null)
			parent.removeChild(element);
			
	}-*/;
	
	private void onTimeout(){
		if (currRequestAction != null  &&  currRequestAction == requests.peek()){
			onFileFailure(new RequestException("Timeout error loading file: " + currRequestAction.getRequest().getUrl()));
			currRequestAction = null;
		}
	}
	
	private void onFileReceive(String text){
		timeoutTimer.cancel();
		currRequestAction = null;
		RequestAction requestAction = requests.poll();
		log("File received: " + requestAction.getRequest().getUrl());
		requestAction.getCallback().onResponseReceived(requestAction.getRequest(), JsInjectFileResponse.createSuccessResponse(text));
		sendNextRequest();
	}
	
	private void onFileFailure(Throwable exception){
		timeoutTimer.cancel();
		currRequestAction = null;
		RequestAction requestAction = requests.poll();
		log("File receive failed: " + requestAction.getRequest().getUrl());
		requestAction.getCallback().onError(requestAction.getRequest(), exception);
		sendNextRequest();
	}

	
	private native void registerGlobalCallback()/*-{
		var instance = this;
		$wnd.jsFileRequestReceiveFile = function(text){
			$entry(instance.@eu.ydp.jsfilerequest.client.jsinject.JsInjectRequestPerformer::onFileReceive(Ljava/lang/String;)(text));
		}
	}-*/;
	
	private native String getFileSuffix()/*-{
		if (typeof $wnd.jsFileRequestSuffix == 'string')
			return $wnd.jsFileRequestSuffix;
		return "";
	}-*/;
	
	
}

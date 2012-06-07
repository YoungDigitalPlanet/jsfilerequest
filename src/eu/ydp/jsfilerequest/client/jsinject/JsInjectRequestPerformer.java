package eu.ydp.jsfilerequest.client.jsinject;

import java.util.Queue;
import java.util.Stack;
import java.util.concurrent.ArrayBlockingQueue;

import com.google.gwt.core.client.Callback;
import com.google.gwt.core.client.ScriptInjector;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.user.client.Timer;

import eu.ydp.jsfilerequest.client.FileRequest;
import eu.ydp.jsfilerequest.client.FileRequestCallback;
import eu.ydp.jsfilerequest.client.RequestAction;

/**
 * Class responsible for managing the queue of the files to load,
 * fetching the file contents and running the callback.
 * 
 * @author Rafal Rybacki rrybacki@ydp.com.pl
 *
 */
class JsInjectRequestPerformer {

	private static JsInjectRequestPerformer instance;
	private int TIMEOUT = 200; // low timeout as the Request Performer will be user only locally
	private Timer timeoutTimer;
	
	Queue<RequestAction> requests = new ArrayBlockingQueue<RequestAction>(0);
	boolean requestUnderway = false;

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
		if (!requestUnderway){
			sendNextRequest();
		}
	}
	
	private void sendNextRequest(){
		if (requests.size() > 0){
			String url = requests.peek().getRequest().getUrl();
			injectScriptNodeIntoDom(url);
			timeoutTimer.schedule(TIMEOUT);
		}
	}
	
	private void injectScriptNodeIntoDom(String scriptUrl){
		ScriptInjector.fromUrl(scriptUrl).setCallback(new Callback<Void, Exception>() {
			
			@Override
			public void onSuccess(Void result) { }
			
			@Override
			public void onFailure(Exception reason) {
				onFileFailure(reason);
			}
		});
		
	}
	
	private void onTimeout(){
		onFileFailure(new RequestException("Timeout error loading file: " + requests.peek().getRequest().getUrl()));
	}
	
	private void onFileReceive(String text){
		timeoutTimer.cancel();
		RequestAction requestAction = requests.poll();
		requestAction.getCallback().onResponseReceived(requestAction.getRequest(), JsInjectFileResponse.createSuccessResponse(text));
		sendNextRequest();
	}
	
	private void onFileFailure(Throwable exception){
		timeoutTimer.cancel();
		RequestAction requestAction = requests.poll();
		requestAction.getCallback().onError(requestAction.getRequest(), exception);
		sendNextRequest();
	}

	
	private native void registerGlobalCallback()/*-{
		var instance = this;
		$wnd.receiveFile = function(text){
			instance.@eu.ydp.jsfilerequest.client.jsinject.JsInjectRequestPerformer::onFileReceive(Ljava/lang/String;)(text);
		}
	}-*/;
	
}

package com.ebttikarat.complaints.client.common;

import com.ebttikarat.complaints.client.processing.IProcessor;

public interface IRequest extends RequestActions {
	
	public static final String MIME_TYPE = "application/json";

	public static final String CONTENT_TYPE = "application/json;charset=UTF-8";
	
	public static final String MOBILE_NUMBER = "mobileNum";

	public static final String ACCESS_TOKEN = "accessToken";
	
	public static final int POST_METHOD = 1;
	
	public static final int PUT_METHOD = 2;
	
	public static final int GET_METHOD = 3;
	
	public IProcessor getProcessor();

	public void setProcessor(IProcessor processor);
	
	/**
	 * sets the value if  the request is (asynchronous) 
	 * 
	 */
	void setAsync(boolean isAsync);

}

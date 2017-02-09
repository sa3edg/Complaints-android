package com.ebttikarat.complaints.client.common;

import java.util.List;

import org.apache.http.NameValuePair;

import com.ebttikarat.complaints.client.processing.IProcessor;


public class Request implements IRequest{
	
	private String orderType = "";
	private String requestURL = "";
	private IProcessor processor = null;	
	private String requestName = "";
	private int requestMethod ;
	private String requestBody = "";
	private boolean isAsync = false;
	private List<NameValuePair> parameters;
	//system orders types
	public static final String SIMPLE_ORDER_TYPE = "SIMPLE_ORDER";
	
	//request default parameter
	public static final String KEY = "key";
	public static final String ACTION = "action";
	
	
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}

	public String getOrderType() {
		return orderType;
	}

	public IProcessor getProcessor() {
		return processor;
	}

	public void setProcessor(IProcessor processor) {
		
		this.processor = processor;
		
	}

	public boolean isAsync() {
		return isAsync;
	}

	public void setAsync(boolean isAsync) {
		this.isAsync = isAsync;
	}

	public String getRequestURL() {
		return requestURL;
	}

	public void setRequestURL(String requestURL) {
		this.requestURL = requestURL;
	}

	public String getRequestName() {
		return requestName;
	}

	public void setRequestName(String requestName) {
		this.requestName = requestName;
	}

	public int getRequestMethod() {
		return requestMethod;
	}

	public void setRequestMethod(int requestMethod) {
		this.requestMethod = requestMethod;
	}

	public String getRequestBody() {
		return requestBody;
	}

	public void setRequestBody(String requestBody) {
		this.requestBody = requestBody;
	}

	public List<NameValuePair> getParameters() {
		return parameters;
	}

	public void setParameters(List<NameValuePair> parameters) {
		this.parameters = parameters;
	}

}

package com.ebttikarat.complaints.client.common;


public class Response implements IResponse{
	
	//the response body which received from the server
	private String responseBody = "";
	private String requestName = "";
	//the json response after remove "result" node from the response
	private String jsonResponse = "";
	//the response type(String, boolean, jsonArray and json-nodes) to be handled
	private String responseType = "";
	private Object responseData;

	public String getRequestName() {
		return requestName;
	}

	public void setRequestName(String requestName) {
		this.requestName = requestName;
	}

	public String getResponseBody() {
		return responseBody;
	}

	public void setResponseBody(String responseBody) {
		this.responseBody = responseBody;
	}

	public String getResponseType() {
		return responseType;
	}

	public void setResponseType(String responseType) {
		this.responseType = responseType;
	}

	public String getJsonResponse() {
		return jsonResponse;
	}

	public void setJsonResponse(String jsonResponse) {
		this.jsonResponse = jsonResponse;
	}

	public Object getResponseData() {
		return responseData;
	}

	public void setResponseData(Object responseData) {
		this.responseData = responseData;
	}
}

package com.ebttikarat.complaints.client;

import static com.ebttikarat.complaints.common.config.Constants.ADD_NEW_COMPLAINTS_SERVICE_URL;
import static com.ebttikarat.complaints.common.config.Constants.GET_UNITS_SERVICE_URL;
import static com.ebttikarat.complaints.common.config.Constants.ADD_COMPLAINTS_ATTACHMENT_SERVICE_URL;
import static com.ebttikarat.complaints.common.config.Constants.GET_ALL_COMPLAINTS_SERVICE_URL;
import static com.ebttikarat.complaints.common.config.Constants.GET_PAGE_CONTENT_SERVICE_URL;
import static com.ebttikarat.complaints.common.config.Constants.RATE_SERVICE_URL;
import static com.ebttikarat.complaints.common.config.Constants.PHONE_REGISTERATION_SERVICE_URL;
import static com.ebttikarat.complaints.common.config.Constants.PHONE_VERIFICATION_SERVICE_URL;
import static com.ebttikarat.complaints.common.config.Constants.ADD_COMPLAINT_COMMENT_SERVICE_URL;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.ebttikarat.complaints.client.common.Request;
import com.ebttikarat.complaints.client.processing.HttpClientLocationProcessor;
import com.ebttikarat.complaints.client.processing.HttpFileUploadProcessor;
import com.ebttikarat.complaints.common.model.Attachment;
import com.ebttikarat.complaints.common.model.Complaint;
import com.ebttikarat.complaints.common.model.User;
import com.ebttikarat.complaints.util.URLEncodingUtil;

public final class RequestFactory {

	private static Request createGeneralRequest(
			String requestBody, String requestName, String serviceURL, int serviceMethod, boolean isAsync) {
		Request request = new Request();
		request.setOrderType(Request.SIMPLE_ORDER_TYPE);
		request.setRequestName(requestName);
		request.setRequestBody(requestBody);
		request.setRequestURL(serviceURL);
		request.setRequestMethod(serviceMethod);
		//Set is Async order to prevent interaction with the GUI
		//should be true if need to  receive a response from the server.
		request.setAsync(isAsync);
		return request;
	}
	
	private static JSONObject createAuthenticationJSONObject()throws Exception{
		JSONObject authenticationObj = new JSONObject();
		authenticationObj.put(Request.MOBILE_NUMBER, User.mySetting.getDefaultMobileNumber().getPhoneNumber());
		authenticationObj.put(Request.ACCESS_TOKEN, User.mySetting.getDefaultMobileNumber().getAccessToken());
//		authenticationObj.put(Request.MOBILE_NUMBER, "0563307435");
//		authenticationObj.put(Request.ACCESS_TOKEN, "123");
		return authenticationObj;
	}
	public static Request addComplaintRequest(Complaint complaint) throws Exception{
		JSONObject requestBody = new JSONObject();
		requestBody.put("user", createAuthenticationJSONObject());
		requestBody.put("complaint", complaint.createJsonRequest());
		String requestBodyString = requestBody.toString();
		return createGeneralRequest(requestBodyString, Request.ADD_COMPLAINT_ORDER, ADD_NEW_COMPLAINTS_SERVICE_URL, Request.POST_METHOD,  false);
	}
	
	public static Request getUnitsRequest() throws Exception{
//		JSONObject requestBody = new JSONObject();
//		requestBody.put("user", createAuthenticationJSONObject());
//		String requestBodyString = requestBody.toString();
		return createGeneralRequest("", Request.GET_UNITS_ORDER, GET_UNITS_SERVICE_URL, Request.POST_METHOD,  false);
	}
	public static Request attachFileRequest(Attachment attachment) throws Exception{
		JSONObject requestBody = new JSONObject();
		requestBody.put("complaintId", attachment.getComplaintId());
		requestBody.put("user", createAuthenticationJSONObject());
		requestBody.put("file", attachment.createJsonRequest());
		String requestBodyString = requestBody.toString();
		return createGeneralRequest(requestBodyString, Request.ATTACH_FILE_ORDER, ADD_COMPLAINTS_ATTACHMENT_SERVICE_URL, Request.POST_METHOD,  false);
	}
	
	public static Request getComplaintsRequest() throws Exception{
		JSONObject requestBody = new JSONObject();
		requestBody.put("user", createAuthenticationJSONObject());
		String requestBodyString = requestBody.toString();
		return createGeneralRequest(requestBodyString, Request.GET_COMPLAINTS_ORDER, GET_ALL_COMPLAINTS_SERVICE_URL, Request.POST_METHOD,  false);
	}
	
	public static Request createMapAPIRequest(String lat, String lon, String zoom, String size)
	{
		String location = lat + "," + lon;
		Request request = new Request();
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(5);   
	    nameValuePairs.add(new BasicNameValuePair("center", location));
	    nameValuePairs.add(new BasicNameValuePair("zoom", zoom));
	    nameValuePairs.add(new BasicNameValuePair("size", size));
	    nameValuePairs.add(new BasicNameValuePair("markers", "color:blue%7Clabel:S%7C" + location));
	    nameValuePairs.add(new BasicNameValuePair("sensor", "true"));
	   

	    request.setRequestURL(URLEncodingUtil.prepareParameters(nameValuePairs));
	    request.setOrderType(Request.USER_LOC_ORDER);
	    request.setProcessor(new HttpClientLocationProcessor());
		request.setRequestName(Request.USER_LOC_ORDER);
		return request;
	}
	
	public static Request getPageContentRequest(String pageId) throws Exception{
		JSONObject requestBody = new JSONObject();
		requestBody.put("pageId", pageId);
//		requestBody.put("user", createAuthenticationJSONObject());
		String requestBodyString = requestBody.toString();
		return createGeneralRequest(requestBodyString, Request.GET_PAGE_CONTENT_ORDER, GET_PAGE_CONTENT_SERVICE_URL, Request.POST_METHOD,  false);
	}
	public static Request addRateRequest(String rateNumber, String comment) throws Exception{
		JSONObject requestBody = new JSONObject();
		JSONObject rateObj = new JSONObject();
		rateObj.put("ratingNum", rateNumber);
		rateObj.put("comment", comment);
		requestBody.put("rating", rateObj);
		requestBody.put("user", createAuthenticationJSONObject());
		String requestBodyString = requestBody.toString();
		return createGeneralRequest(requestBodyString, Request.ADD_RATE_ORDER, RATE_SERVICE_URL, Request.POST_METHOD,  false);
	}
	public static Request registerMobileNumber(String number) throws Exception{
		JSONObject requestBody = new JSONObject();
		requestBody.put("mobileNo", number);
		String requestBodyString = requestBody.toString();
		return createGeneralRequest(requestBodyString, Request.REGISTER_PHONE_ORDER, PHONE_REGISTERATION_SERVICE_URL, Request.POST_METHOD,  false);
	}
	public static Request verifyMobileNumber(String mobileNumber, String activationCode) throws Exception{
		JSONObject requestBody = new JSONObject();
		requestBody.put("mobileNo", mobileNumber);
		requestBody.put("accessToken", activationCode);
		String requestBodyString = requestBody.toString();
		return createGeneralRequest(requestBodyString, Request.VERIFY_PHONE_ORDER, PHONE_VERIFICATION_SERVICE_URL, Request.POST_METHOD,  false);
	}
	public static Request addCommentRequest(String complaintId, String comment) throws Exception{
		JSONObject requestBody = new JSONObject();
		JSONObject rateObj = new JSONObject();
		rateObj.put("complaintId", complaintId);
		rateObj.put("comment", comment);
		requestBody.put("comment", rateObj);
		requestBody.put("user", createAuthenticationJSONObject());
		String requestBodyString = requestBody.toString();
		return createGeneralRequest(requestBodyString, Request.ADD_COMMENT_ORDER, ADD_COMPLAINT_COMMENT_SERVICE_URL, Request.POST_METHOD,  false);
	}
	
	public static Request uploadFileRequest(String complaintId, String filePath) {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new BasicNameValuePair("path", filePath));
		Request request = new Request();
		request.setParameters(nameValuePairs);
		request.setOrderType(Request.SIMPLE_ORDER_TYPE);
		request.setRequestName(Request.ATTACH_FILE_ORDER);
		request.setRequestBody("");
		request.setRequestURL(ADD_COMPLAINTS_ATTACHMENT_SERVICE_URL + complaintId);
		request.setRequestMethod(Request.POST_METHOD);
		request.setOrderType(Request.ATTACH_FILE_ORDER);
		request.setProcessor(new HttpFileUploadProcessor());
		//Set is Async order to prevent interaction with the GUI
		//should be true if need to  receive a response from the server.
		request.setAsync(false);
		return request;
	}

}

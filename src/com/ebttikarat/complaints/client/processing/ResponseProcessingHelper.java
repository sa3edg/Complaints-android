package com.ebttikarat.complaints.client.processing;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import com.ebttikarat.complaints.client.common.IResponse;
import com.ebttikarat.complaints.client.common.Response;
import com.ebttikarat.complaints.common.model.Complaint;
import com.ebttikarat.complaints.common.model.IModel;
import com.ebttikarat.complaints.common.model.Unit;
import com.ebttikarat.complaints.exception.IlligalResponseType;

public class ResponseProcessingHelper {

	private static ResponseProcessingHelper self = null;

	private ResponseProcessingHelper() {

	}

	public static ResponseProcessingHelper getInstance() {
		if (self == null) {
			self = new ResponseProcessingHelper();
		}
		return self;
	}

	public Object handleResponse(Response response,IModel modelType, Type collectionType)
			throws Exception {

		// JSONArray arr = new JSONArray(response);
		JSONObject json = new JSONObject(response.getResponseBody());// arr.getJSONObject(0);
		String responseStatus = json.getString("Error");

		if(responseStatus.equals(Response.ERROR))
		{
			throw new Exception();
		}
		Log.i("response code", responseStatus);
		String jsonArray = json.getString("data");

		Log.i("response array", jsonArray);

		response.setJsonResponse(jsonArray);
		// handle json array response
	    if (IResponse.RESPONSE_TYPE_JSON_ARRAY.equals(response.getResponseType())) {

			return handleJsonArrayResponse(response, collectionType);
		}
	    // handle json array response
	    else if (IResponse.RESPONSE_TYPE_JSON_OBJECT.equals(response.getResponseType())) {

				return handleJsonObjectResponse(response, modelType);
			}
	    // handle json nodes response
	    else if (IResponse.RESPONSE_TYPE_JSON_NODES.equals(response.getResponseType())) {

			return handleJsonNodesResponse(jsonArray);
		}
		// handle boolean value in response node
		else if (IResponse.RESPONSE_TYPE_BOOLEAN.equals(response.getResponseType())) {

			return Boolean.valueOf(jsonArray);
		}
		// handle String value in response node
		else if (IResponse.RESPONSE_TYPE_STRING.equals(response.getResponseType())) {

			return new String(jsonArray);
		}
		
		return null;
	}

	private List<? extends IModel> handleJsonArrayResponse(Response jsonArray, Type collectionType)
			throws Exception {
		if(collectionType == null)
		{
			throw new IlligalResponseType();
		}
		
		List<? extends IModel> poolsList = new Gson().fromJson(jsonArray.getJsonResponse(), collectionType);

		return poolsList;
	}
	
	private IModel handleJsonObjectResponse(Response jsonArray, IModel modelType)
			throws Exception {
		if(modelType == null)
		{
			throw new IlligalResponseType();
		}
		
		IModel model = new Gson().fromJson(jsonArray.getJsonResponse(), modelType.getClass());
		return model;
	}
	
	
	private String[] handleJsonNodesResponse(String jsonArray)
			throws Exception {
		if(jsonArray == null || "".equals(jsonArray))
		{
			return null;
		}
		String[] updates = jsonArray.split(",");
		String[] userUpdates = new String[3];
		userUpdates[0] = updates[0].substring(updates[0].indexOf("=") + 1, updates[0].length());
		userUpdates[1] = updates[1].substring(updates[1].indexOf("=") + 1, updates[1].length());
		userUpdates[2] = updates[2].substring(updates[2].indexOf("=") + 1, updates[2].length());
		return userUpdates;
	}
	
	public String handleAddComplaintResponse(Response response)
			throws Exception {
		JSONObject json = new JSONObject(response.getResponseBody());
		String responseStatus = json.getString("Complaints");
		JSONObject complaintObj = new JSONObject(responseStatus);
		String errorText = complaintObj.getString("error");
		JSONObject errorObj = new JSONObject(errorText);
		String hasError = errorObj.getString("haserror");
		if(hasError.equals("false")){
			return complaintObj.getString("ComplaintID");
		}
		return null;
	}
	
	public List<Unit> handleGetUnitsResponse(Response response)
			throws Exception {
		JSONObject json = new JSONObject(response.getResponseBody());
		String responseStatus = json.getString("destinationUnits");
		JSONObject complaintObj = new JSONObject(responseStatus);
		String errorText = complaintObj.getString("error");
		JSONObject errorObj = new JSONObject(errorText);
		String hasError = errorObj.getString("haserror");
		if(hasError.equals("false")){
			String Units = complaintObj.getString("units");
			Type collectionType = new TypeToken<ArrayList<Unit>>() {
			}.getType();
			List<Unit> unitList = new Gson().fromJson(Units, collectionType);
			return unitList;
		}
		return null;
	}
	public String handleAttachFileResponse(Response response)
			throws Exception {
		if("\"success\"".equals(response.getResponseBody())){
			return "fileId";
		}
		return null;
	}
	public List<Complaint> handleGetComplaintsResponse(Response response)
			throws Exception {
		JSONObject json = new JSONObject(response.getResponseBody());
		String responseStatus = json.getString("Complaints");
		JSONObject complaintObj = new JSONObject(responseStatus);
		String errorText = complaintObj.getString("error");
		JSONObject errorObj = new JSONObject(errorText);
		String hasError = errorObj.getString("haserror");
		if("".equals(hasError) || hasError.equals("false")){
			String complaints = complaintObj.getString("Complaints");
			Type collectionType = new TypeToken<ArrayList<Complaint>>() {
			}.getType();
			List<Complaint> unitList = new Gson().fromJson(complaints, collectionType);
			return unitList;
		}
		return null;
	}
	public String handleGetContentResponse(Response response)
			throws Exception {
		JSONObject json = new JSONObject(response.getResponseBody());
		String responseStatus = json.getString("content");
		JSONObject complaintObj = new JSONObject(responseStatus);
		String errorText = complaintObj.getString("error");
		JSONObject errorObj = new JSONObject(errorText);
		String hasError = errorObj.getString("haserror");
		if("".equals(hasError) || hasError.equals("false")){
			return complaintObj.getString("content");
		}
		return null;
	}
	public String handleRateResponse(Response response)
			throws Exception {
		JSONObject json = new JSONObject(response.getResponseBody());
		String responseStatus = json.getString("Rating");
		JSONObject complaintObj = new JSONObject(responseStatus);
		String errorText = complaintObj.getString("error");
		JSONObject errorObj = new JSONObject(errorText);
		String hasError = errorObj.getString("haserror");
		if("".equals(hasError) || hasError.equals("false") || "null".equals(hasError)){
			return complaintObj.getString("RatingID");
		}
		return null;
	}
	
	public String handlePhoneRegistrationResponse(Response response)
			throws Exception {
		JSONObject json = new JSONObject(response.getResponseBody());
		String responseStatus = json.getString("RegisterNewPhoneResult");
		JSONObject complaintObj = new JSONObject(responseStatus);
		String errorText = complaintObj.getString("error");
		JSONObject errorObj = new JSONObject(errorText);
		String hasError = errorObj.getString("haserror");
		if("".equals(hasError) || hasError.equals("false")){
			return "true";
		}
		return null;
	}
	public String handlePhoneVerificationResponse(Response response)
			throws Exception {
		JSONObject json = new JSONObject(response.getResponseBody());
		return json.getString("VerifyMobile");
	}
	public String handleAddComment(Response response)
			throws Exception {
		JSONObject json = new JSONObject(response.getResponseBody());
		String responseStatus = json.getString("AddComment");
		JSONObject complaintObj = new JSONObject(responseStatus);
		String errorText = complaintObj.getString("error");
		JSONObject errorObj = new JSONObject(errorText);
		String hasError = errorObj.getString("haserror");
		if("".equals(hasError) || hasError.equals("false")){
			return complaintObj.getString("commentID");
		}
		return null;
	}

}

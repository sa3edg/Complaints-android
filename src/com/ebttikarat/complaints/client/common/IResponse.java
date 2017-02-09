package com.ebttikarat.complaints.client.common;

public interface IResponse {
	public static final String RESPONSE_TYPE_STRING = "string";
    public static final String RESPONSE_TYPE_BOOLEAN = "boolean";
    public static final String RESPONSE_TYPE_JSON_OBJECT = "json_object";
    public static final String RESPONSE_TYPE_JSON_ARRAY = "json_array";
    public static final String RESPONSE_TYPE_JSON_NODES = "json_nodes";
    
    public static final String REQUEST_METHOD_ERROR = "not supported method";
    public static final String ERROR = "error";
}

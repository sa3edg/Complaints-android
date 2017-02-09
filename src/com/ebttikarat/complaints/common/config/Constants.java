package com.ebttikarat.complaints.common.config;

import com.ebttikarat.complaints.gui.R;

import android.content.res.Resources;

public class Constants {

    public static Resources config = null;
    public static String SERVER_URL = "";
    public static String ADD_NEW_COMPLAINTS_SERVICE_URL = "";
    public static String GET_UNITS_SERVICE_URL = "";
    public static String ADD_COMPLAINTS_ATTACHMENT_SERVICE_URL = "";
    public static String ADD_COMPLAINT_COMMENT_SERVICE_URL = "";
    public static String GET_ALL_COMPLAINTS_SERVICE_URL = "";
    public static String GET_COMPLAINT_SERVICE_URL = "";
    public static String RATE_SERVICE_URL = "";
    public static String GET_PAGE_CONTENT_SERVICE_URL = "";
    public static String PHONE_REGISTERATION_SERVICE_URL = "";
    public static String PHONE_VERIFICATION_SERVICE_URL = "";

	public static synchronized void init(Resources res) throws Exception{
		config = res;
		SERVER_URL = config.getString(R.string.server_url);
		ADD_NEW_COMPLAINTS_SERVICE_URL = config.getString(R.string.add_new_complaint_service_url);
		GET_UNITS_SERVICE_URL = config.getString(R.string.get_units_service_url);
		ADD_COMPLAINTS_ATTACHMENT_SERVICE_URL = config.getString(R.string.add_complaint_attachment_service_url);
		ADD_COMPLAINT_COMMENT_SERVICE_URL = config.getString(R.string.add_complaint_comment_service_url);
		GET_ALL_COMPLAINTS_SERVICE_URL = config.getString(R.string.get_all_complaints_service_url);
		GET_COMPLAINT_SERVICE_URL = config.getString(R.string.get_complaint_service_url);
		RATE_SERVICE_URL = config.getString(R.string.rate_service_url);
		GET_PAGE_CONTENT_SERVICE_URL = config.getString(R.string.page_content_service_url);
		PHONE_REGISTERATION_SERVICE_URL = config.getString(R.string.phone_registeration_service_url);
		PHONE_VERIFICATION_SERVICE_URL = config.getString(R.string.phone_verification_service_url);
	}
}

package com.ebttikarat.complaints.gui;

import com.ebttikarat.complaints.client.common.Response;


public interface IActivity {
	
	/**
	 * <pre>
	 * -execution event
	 * @param response
	 */
	void preExecution();

	/**
	 * post execution event
	 * 
	 * @param response
	 */
	void postExecution(Response response);

}

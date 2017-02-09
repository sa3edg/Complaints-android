package com.ebttikarat.complaints.common.model;

import java.util.HashMap;
import java.util.Map;


public class User extends Model{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2201099265970968322L;
	public static Setting mySetting = new Setting();
	public static Map<String, Unit> destinationUnitMap = new HashMap<String, Unit>();
}

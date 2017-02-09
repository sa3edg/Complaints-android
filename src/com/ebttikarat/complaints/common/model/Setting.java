package com.ebttikarat.complaints.common.model;

import java.util.ArrayList;
import java.util.List;

import com.ebttikarat.complaints.common.model.Model;
import com.ebttikarat.complaints.gui.model.PhoneNumber;

public class Setting extends Model{


	/**
	 * 
	 */
	private static final long serialVersionUID = 1011232379302975282L;
	private List<PhoneNumber> mobileNumbers = new ArrayList<PhoneNumber>();
	private PhoneNumber defaultMobileNumber ;
	public List<PhoneNumber> getMobileNumbers() {
		return mobileNumbers;
	}
	public void setMobileNumbers(List<PhoneNumber> mobileNumbers) {
		this.mobileNumbers = mobileNumbers;
	}
	public PhoneNumber getDefaultMobileNumber() {
		return defaultMobileNumber;
	}
	public void setDefaultMobileNumber(PhoneNumber defaultMobileNumber) {
		this.defaultMobileNumber = defaultMobileNumber;
	}

}

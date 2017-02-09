package com.ebttikarat.complaints.gui.model;

public class PhoneNumber {
	
	private String phoneNumber = "";
	private String accessToken = "";
	private String isVerified = "0";
	private String isPrefered = "0";
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}
	public String getAccessToken() {
		return accessToken;
	}
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
	public String getIsVerified() {
		return isVerified;
	}
	public void setIsVerified(String isVerified) {
		this.isVerified = isVerified;
	}
	public String getIsPrefered() {
		return isPrefered;
	}
	public void setIsPrefered(String isPrefered) {
		this.isPrefered = isPrefered;
	}

}

package com.ebttikarat.complaints.common.model;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class Complaint extends Model implements Parcelable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String id;

	private String title = "";
	private String status = "";
	private String closeReason = "";
	private String creationDate = "";
	private String lastUpdateDate = "";
	private String creationDateHijri = "";
	private String lastUpdateDateHijri = "";
	private String description = "";
	private String latitude = "";
	private String longitude = "";
	private String locationDescription = "";
	private String destinationUnit = "";
	private String unitId = "";
	private List<Attachment> files = new ArrayList<Attachment>();

	/**
	 * default constructor
	 */
	public Complaint(){
		
	}
	
	/**
	 * Parcel constructor
	 * @param in
	 */
    public Complaint(Parcel in){
        String[] data = new String[2];
        in.readStringArray(data);
        this.id = data[0];
        this.title = data[1];
    }
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		// dest.writeString(phone);
		dest.writeStringArray(new String[] { this.getId(), this.title });
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCloseReason() {
		return closeReason;
	}

	public void setCloseReason(String closeReason) {
		this.closeReason = closeReason;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
	}

	public String getLastUpdateDate() {
		return lastUpdateDate;
	}

	public void setLastUpdateDate(String lastUpdateDate) {
		this.lastUpdateDate = lastUpdateDate;
	}

	public String getCreationDateHijri() {
		return creationDateHijri;
	}

	public void setCreationDateHijri(String creationDateHijri) {
		this.creationDateHijri = creationDateHijri;
	}

	public String getLastUpdateDateHijri() {
		return lastUpdateDateHijri;
	}

	public void setLastUpdateDateHijri(String lastUpdateDateHijri) {
		this.lastUpdateDateHijri = lastUpdateDateHijri;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLocationDescription() {
		return locationDescription;
	}

	public void setLocationDescription(String locationDescription) {
		this.locationDescription = locationDescription;
	}

	public String getDestinationUnit() {
		return destinationUnit;
	}

	public void setDestinationUnit(String destinationUnit) {
		this.destinationUnit = destinationUnit;
	}

	public List<Attachment> getFiles() {
		return files;
	}

	public void setFiles(List<Attachment> files) {
		this.files = files;
	}
	
	public JSONObject createJsonRequest(){
		try{
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("title", getTitle());
			jsonObj.put("description", getDescription());
			jsonObj.put("latitude", getLatitude());
			jsonObj.put("longitude", getLongitude());
			jsonObj.put("locationDescription", getLocationDescription());
			jsonObj.put("destinationUnit", getDestinationUnit());
			return jsonObj;
		}
		catch(Exception ex){
			
		}
		return null;
	}

	public String getUnitId() {
		return unitId;
	}

	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}

	/** 
	 * Static field used to regenerate object, individually or as arrays
	 * 
	 **/
	public static final Parcelable.Creator<Complaint> CREATOR = new Parcelable.Creator<Complaint>() {
		public Complaint createFromParcel(Parcel pc) {
			return new Complaint(pc);
		}

		public Complaint[] newArray(int size) {
			return new Complaint[size];
		}
	};

}

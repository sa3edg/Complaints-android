package com.ebttikarat.complaints.common.model;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

import com.ebttikarat.complaints.gui.model.FileBrowserItem;

public class Attachment extends Model{

	/**
	 * 
	 */
	private static final long serialVersionUID = 9025680436501025953L;
	private String fileId = "";
	private String complaintId;
	private String fileName = "";
	private String fileType = "";
	private String filePath;
	private byte[] fileData;
	public JSONObject createJsonRequest(){
		try{
			JSONObject jsonObj = new JSONObject();
			jsonObj.put("fileName", getFileName());
			jsonObj.put("fileType", getFileType());
			jsonObj.put("fileData", getFileData());
			return jsonObj;
		}
		catch(Exception ex){
			
		}
		return null;
	}
	public String getFileId() {
		return fileId;
	}
	public void setFileId(String fileId) {
		this.fileId = fileId;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public String getComplaintId() {
		return complaintId;
	}
	public void setComplaintId(String complaintId) {
		this.complaintId = complaintId;
	}
	public byte[] getFileData() {
		return fileData;
	}
	public void setFileData(byte[] fileData) {
		this.fileData = fileData;
	}
	
	public FileBrowserItem getFileBrowserItem(){
		FileBrowserItem item = new FileBrowserItem(this.fileName, "", "", filePath, "", fileData);
		
		try {
			File ff = new File(this.filePath);
			byte[] data = FileUtils.readFileToByteArray(ff);
			item.setFileData(data);
			item.setVideo(com.ebttikarat.complaints.util.FileUtils.isVideo(filePath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
		}
		return item;
	}

}

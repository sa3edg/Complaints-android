package com.ebttikarat.complaints.gui.model;

import com.ebttikarat.complaints.common.model.Attachment;
import com.ebttikarat.complaints.util.FileUtils;

public class FileBrowserItem implements Comparable<FileBrowserItem>{
	private String name;
	private String data;
	private String date;
	private String path;
	private String image;
	private byte[] fileData;
	private boolean isVideo = false;
	
	public FileBrowserItem(String n,String d, String dt, String p, String img, byte[] fData)
	{
		name = n;
		data = d;
		date = dt;
		path = p; 
		image = img;
		setFileData(fData);
		
	}
	public String getName()
	{
		return name;
	}
	public String getData()
	{
		return data;
	}
	public String getDate()
	{
		return date;
	}
	public String getPath()
	{
		return path;
	}
	public String getImage() {
		return image;
	}
	
	public int compareTo(FileBrowserItem o) {
		if(this.name != null)
			return this.name.toLowerCase().compareTo(o.getName().toLowerCase()); 
		else 
			throw new IllegalArgumentException();
	}
	public boolean isVideo() {
		return isVideo;
	}
	public void setVideo(boolean isVideo) {
		this.isVideo = isVideo;
	}
	public byte[] getFileData() {
		return fileData;
	}
	public void setFileData(byte[] fileData) {
		this.fileData = fileData;
	}
	public Attachment getAttachment(String complaintId){
		Attachment attachment = new Attachment();
		attachment.setFileData(getFileData());
		attachment.setFileName(getName());
		attachment.setFileType(FileUtils.getFileType(getName()));
		return attachment;
	}
}

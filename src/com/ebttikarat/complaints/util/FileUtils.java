package com.ebttikarat.complaints.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class FileUtils {
	private static final String IMAGE_PATTERN = "([^\\s]+(\\.(?i)(/gif|jpg|bmp|png))$)";
	private static final String VIDEO_PATTERN = "([^\\s]+(\\.(?i)(/mpg4|mp4|3gp))$)";

	public static String getFileName(String path) {
		if (path == null || "".equals(path)) {
			return "";
		}
		return path.substring(path.lastIndexOf("\\") + 1);
	}

	public static String getFileType(String path) {
		if (path == null || "".equals(path)) {
			return "";
		}
		return path.substring(path.lastIndexOf("."));
	}

	public static String getRealImagePathFromURI(Context context, Uri contentURI) {
		if(contentURI == null){
			return "";
		}
		Cursor cursor = null;
		try {
			cursor = context.getContentResolver().query(contentURI, null, null, null, null);
		    if (cursor == null) { // Source is Dropbox or other similar local file path
		        return contentURI.getPath();
		    } else { 
		        cursor.moveToFirst(); 
		        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA); 
		        return cursor.getString(idx);
		    }
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}
	
	public static String getRealVideoPathFromURI(Context context, Uri contentURI) {
		if(contentURI == null){
			return "";
		}
		Cursor cursor = null;
		try {
			cursor = context.getContentResolver().query(contentURI, null, null, null, null);
		    if (cursor == null) { // Source is Dropbox or other similar local file path
		        return contentURI.getPath();
		    } else { 
		        cursor.moveToFirst(); 
		        int idx = cursor.getColumnIndex(MediaStore.Video.VideoColumns.DATA); 
		        return cursor.getString(idx);
		    }
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}

	public static boolean isImage(final String imagePath) {
		Pattern pattern = Pattern.compile(IMAGE_PATTERN);
		Matcher matcher = pattern.matcher(imagePath);
		return matcher.matches();
	}

	public static boolean isVideo(final String videoPath) {
		Pattern pattern = Pattern.compile(VIDEO_PATTERN);
		Matcher matcher = pattern.matcher(videoPath);
		return matcher.matches();
	}

}

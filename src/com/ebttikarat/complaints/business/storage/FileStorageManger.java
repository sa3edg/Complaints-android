package com.ebttikarat.complaints.business.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.net.Uri;
import android.os.Environment;
import android.util.Log;

public class FileStorageManger {
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;
    private static String TAG = "FILE_STORAGE";

	private static final String APP_DIR = "complaints";

	/** Create a file Uri for saving an image or video */
	public static Uri getOutputMediaFileUri(int type, String fileName) {
		File mediaFile = getOutputMediaFile(type, fileName);
		return mediaFile != null ? Uri.fromFile(mediaFile) : null;
	}

	public static void copyDirectory(File sourceLocation, File targetLocation)
			throws IOException {

		if (sourceLocation.isDirectory()) {
			if (!targetLocation.exists()) {
				targetLocation.mkdir();
			}

			String[] children = sourceLocation.list();
			for (int i = 0; i < children.length; i++) {
				copyDirectory(new File(sourceLocation, children[i]), new File(
						targetLocation, children[i]));
			}
		} else {
			InputStream in = null;
			OutputStream out = null;
			try {
				in = new FileInputStream(sourceLocation);
				out = new FileOutputStream(targetLocation);

				// Copy the bits from instream to outstream
				byte[] buf = new byte[1024];
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
			} catch (Exception ex) {

			} finally {
				if (in != null) {
					in.close();
				}
				if (out != null) {
					out.close();
				}
			}
		}
	}
	
	public static boolean saveFile(String fileName, byte[] data, boolean isVideo){
		int fileType = isVideo ? MEDIA_TYPE_VIDEO : MEDIA_TYPE_IMAGE;
		File pictureFile = FileStorageManger.getOutputMediaFile(fileType, fileName);
        if (pictureFile == null){
            return false;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            fos.write(data);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
        return true;
	}

	/** Create a File for saving an image or video */
	public static File getOutputMediaFile(int type, String fileName) {
		// To be safe, you check that the SDCard is mounted
		if (!Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			return null;
		}

		File mediaStorageDir = new File(
				Environment.getExternalStorageDirectory(),
				APP_DIR);
		// This location works best if you want the created images to be shared
		// between applications and persist after your app has been uninstalled.

		// Create the storage directory if it does not exist
		if (!mediaStorageDir.exists()) {
			if (!mediaStorageDir.mkdirs()) {
				Log.d(APP_DIR, "failed to create directory");
				return null;
			}
		}

		// Create a media file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
		.format(new Date());
		File mediaFile;
		if (type == MEDIA_TYPE_IMAGE) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ timeStamp + ".jpg");
		} else if (type == MEDIA_TYPE_VIDEO) {
			mediaFile = new File(mediaStorageDir.getPath() + File.separator
					+ timeStamp + ".mp4");
		} else {
			return null;
		}

		return mediaFile;
	}
}

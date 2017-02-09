package com.ebttikarat.complaints.business.media;

import android.app.Activity;

public abstract class MediaCapture implements IMedia{

	public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	public static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
	protected Activity activity;
	public MediaCapture(Activity activity){
		this.activity = activity;
	}
}

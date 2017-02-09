package com.ebttikarat.complaints.gui.view;


import java.io.IOException;

import com.ebttikarat.complaints.business.media.CameraPreview;
import com.ebttikarat.complaints.business.storage.FileStorageManger;
import com.ebttikarat.complaints.client.common.Response;
import com.ebttikarat.complaints.gui.AbstractFragment;
import com.ebttikarat.complaints.gui.R;
import com.ebttikarat.complaints.gui.model.FileBrowserItem;

import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

public class CameriaPreviewViewView extends AbstractFragment {

	private AddNewComplaintView parentFg;
	private boolean isVideoMode = false;
	private static Camera mCamera;
	private MediaRecorder mMediaRecorder;
	private boolean isRecording = false;
    private CameraPreview mPreview;
    private static String TAG = "Camera perview view";
	public CameriaPreviewViewView(MainView mView, AddNewComplaintView parentFg, boolean isVideoMode){
		super(mView);
		this.parentFg = parentFg;
		this.isVideoMode = isVideoMode;
		// TODO Auto-generated constructor stub
	}

	public CameriaPreviewViewView(MainView mView) {
		super(mView);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.media_capture_layout,
				container, false);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedState) {
		super.onActivityCreated(savedState);
		// Create an instance of Camera
        getCameraInstance();

        // Create our Preview view and set it as the content of our activity.
        mPreview = new CameraPreview(activity, mCamera);
        FrameLayout preview = (FrameLayout) activity.findViewById(R.id.camera_preview);
        preview.addView(mPreview);
        mCamera.setDisplayOrientation(90);
		ImageButton capture = (ImageButton) activity
				.findViewById(R.id.captureBtn);

		capture.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				
				if(isVideoMode){
					recordVideoEvent();
				}
				else{
					mCamera.takePicture(null, null, mPicture);
				}
			}

		});
	}

	/** A safe way to get an instance of the Camera object. */
	private void getCameraInstance(){
	    try {
            mCamera = Camera.open(); // attempt to get a Camera instance
	    }
	    catch (Exception e){
	        // Camera is not available (in use or does not exist)
	    	Toast.makeText(activity, "Camera not avialable", Toast.LENGTH_LONG).show();
	    }
	}
	private void recordVideoEvent(){
		if (isRecording) {
            // stop recording and release camera
            mMediaRecorder.stop();  // stop the recording
            releaseMediaRecorder(); // release the MediaRecorder object
            mCamera.lock();         // take camera access back from MediaRecorder

            // inform the user that recording has stopped
            //setCaptureButtonText("Capture");
            isRecording = false;
        } else {
            // initialize video camera
            if (prepareVideoRecorder()) {
                // Camera is available and unlocked, MediaRecorder is prepared,
                // now you can start recording
                mMediaRecorder.start();

                // inform the user that recording has started
                //setCaptureButtonText("Stop");
                isRecording = true;
            } else {
                // prepare didn't work, release the camera
                releaseMediaRecorder();
                // inform user
            }
        }
	}
	private boolean prepareVideoRecorder(){

	    getCameraInstance();
	    mMediaRecorder = new MediaRecorder();

	    // Step 1: Unlock and set camera to MediaRecorder
	    mCamera.unlock();
	    mMediaRecorder.setCamera(mCamera);

	    // Step 2: Set sources
	    mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
	    mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

	    
	    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
	    	// Step 3: Set a CamcorderProfile (requires API Level 8 or higher)
	    	mMediaRecorder.setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_HIGH));
	    }
	    else
	    {
	    	// Step 3: Set output format and encoding (for versions prior to API Level 8)
	    	mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
	    	mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
	    	mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.DEFAULT);
	    }
	    // Step 4: Set output file
	    mMediaRecorder.setOutputFile(FileStorageManger.getOutputMediaFile(FileStorageManger.MEDIA_TYPE_VIDEO, "Said").toString());

	    // Step 5: Set the preview output
	    mMediaRecorder.setPreviewDisplay(mPreview.getHolder().getSurface());

	    // Step 6: Prepare configured MediaRecorder
	    try {
	        mMediaRecorder.prepare();
	    } catch (IllegalStateException e) {
	        Log.d(TAG, "IllegalStateException preparing MediaRecorder: " + e.getMessage());
	        releaseMediaRecorder();
	        return false;
	    } catch (IOException e) {
	        Log.d(TAG, "IOException preparing MediaRecorder: " + e.getMessage());
	        releaseMediaRecorder();
	        return false;
	    }
	    return true;
	}
	private PictureCallback mPicture = new PictureCallback() {

	    @Override
	    public void onPictureTaken(byte[] data, Camera camera) {
	    	try{
	    		FileStorageManger.saveFile("temp", data, isVideoMode);
				parentFg.getMainView().displayAddNewComplaintView(parentFg, new FileBrowserItem("", "", "", "", "",data));
	    	}
	    	catch(Exception ex){
	    		Log.d(TAG, "Error saving file: " + ex.getMessage());
	    		Toast.makeText(activity, "can not save file", Toast.LENGTH_LONG).show();
	    	}
	    	finally{
	    		mCamera.release();
	    	}
	        
	    }
	};
	@Override
    public void onPause() {
        super.onPause();
        releaseMediaRecorder();       // if you are using MediaRecorder, release it first
        releaseCamera();              // release the camera immediately on pause event
    }

    private void releaseMediaRecorder(){
        if (mMediaRecorder != null) {
            mMediaRecorder.reset();   // clear recorder configuration
            mMediaRecorder.release(); // release the recorder object
            mMediaRecorder = null;
            mCamera.lock();           // lock camera for later use
        }
    }

    private void releaseCamera(){
        if (mCamera != null){
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }
	

	@Override
	public void preExecution() {
		// TODO Auto-generated method stub

	}

	@Override
	public void postExecution(Response response) {
		// TODO Auto-generated method stub

	}
}

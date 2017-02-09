package com.ebttikarat.complaints.gui.view;



import com.ebttikarat.complaints.client.common.Response;
import com.ebttikarat.complaints.gui.AbstractFragment;
import com.ebttikarat.complaints.gui.R;
import com.ebttikarat.complaints.gui.adapter.AttachmentListAdapter;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.ImageButton;

public class AttachmentsView extends AbstractFragment {

    private MainView mView;
    private boolean isEditMode;
	public AttachmentsView(MainView mView, boolean isEditMode) {
		super(mView);
		this.mView = mView;
		this.isEditMode = isEditMode;
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.attachment_view_layout,
				container, false);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedState) {
		super.onActivityCreated(savedState);
		// Create grid view
		GridView gridView = (GridView) mView.findViewById(R.id.attachmentGridView);
		AttachmentListAdapter customGridAdapter;
		if(isEditMode){
		    customGridAdapter = new AttachmentListAdapter(mView, R.layout.attachment_row_layout, isEditMode);
		}else{
			customGridAdapter = new AttachmentListAdapter(mView, R.layout.attachment_row_layout);
		}
		gridView.setAdapter(customGridAdapter);
		ImageButton browse = (ImageButton) activity
				.findViewById(R.id.browseBtn);

		browse.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				showFIleBrowser();
			}

		});
		
		ImageButton capturePhoto = (ImageButton) activity
				.findViewById(R.id.captureImageBtn);
		
		capturePhoto.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				if(isEditMode){
					showCameraViewInEditMode(false);
				}else{
					showCameraView(false);
				}
			}

		});
		
		ImageButton captureVideo = (ImageButton) activity
				.findViewById(R.id.captureVideoBtn);

		captureVideo.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				if(isEditMode){
					showCameraViewInEditMode(true);
				}else{
					showCameraView(true);
				}
			}

		});
		ImageButton done = (ImageButton) activity
				.findViewById(R.id.done);

		done.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				if(isEditMode){
					showEditAcomplaint();
				}else{
					showAddAcomplaint();
				}
			}

		});
       
	}

	private void showFIleBrowser(){
		mView.displayFileBrowser(mView.getCurrentAddComplaint(), true, isEditMode);
	}
	private void showAddAcomplaint(){
		mView.displayAddNewComplaintView(mView.getCurrentAddComplaint(), null);
	}
	private void showEditAcomplaint(){
		mView.displayEditComplaintView(mView.getCurrentEditComplaint(), null);
	}
	private void showCameraView(boolean isVideo){
		mView.displayCameraView(mView.getCurrentAddComplaint(), this, isVideo);
	}
	private void showCameraViewInEditMode(boolean isVideo){
		mView.displayCameraViewInEditMode(mView.getCurrentEditComplaint(), this, isVideo);
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

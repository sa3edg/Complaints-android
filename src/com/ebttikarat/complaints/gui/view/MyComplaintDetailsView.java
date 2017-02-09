package com.ebttikarat.complaints.gui.view;

import com.ebttikarat.complaints.client.OrderCoordinator;
import com.ebttikarat.complaints.client.RequestFactory;
import com.ebttikarat.complaints.client.common.Request;
import com.ebttikarat.complaints.client.common.Response;
import com.ebttikarat.complaints.client.processing.ResponseProcessingHelper;
import com.ebttikarat.complaints.common.model.Complaint;
import com.ebttikarat.complaints.gui.AbstractFragment;
import com.ebttikarat.complaints.gui.CustomProgressDialog;
import com.ebttikarat.complaints.gui.R;
import com.ebttikarat.complaints.services.GPSLocationManager;
import com.ebttikarat.complaints.util.GenericUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MyComplaintDetailsView extends AbstractFragment {

	private MainView mView;
	private Complaint complaint;
	private CustomProgressDialog progress;
	private ImageView currentLocationImage;

	public MyComplaintDetailsView(MainView mView, Complaint complaints) {
		super(mView);
		this.mView = mView;
		this.complaint = complaints;
		progress = new CustomProgressDialog(mView);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.my_complaint_details_layout,
				container, false);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedState) {
		super.onActivityCreated(savedState);
		final EditText rateComment = (EditText) activity
				.findViewById(R.id.complaintCommentTxt);
		final TextView complaintNumber = (TextView) activity
				.findViewById(R.id.complaintNumber);
		final TextView complaintTitleValue = (TextView) activity
				.findViewById(R.id.complaintTitleValue);
		final TextView targetValue = (TextView) activity
				.findViewById(R.id.targetValue);
		final TextView dateValue = (TextView) activity
				.findViewById(R.id.dateValue);
		final TextView statusValue = (TextView) activity
				.findViewById(R.id.statusValue);
		final TextView reasonValue = (TextView) activity
				.findViewById(R.id.reasonValue);
		final TextView attachmentCount = (TextView) activity
				.findViewById(R.id.notification);
		complaintNumber.setText(complaint.getId());
		complaintTitleValue.setText(complaint.getTitle());
		targetValue.setText(complaint.getDestinationUnit());
		dateValue.setText(complaint.getCreationDate());
		statusValue.setText(complaint.getStatus());
		reasonValue.setText(complaint.getCloseReason());
		attachmentCount.setText(String.valueOf(complaint.getFiles().size()));
		currentLocationImage = (ImageView) activity
				.findViewById(R.id.currentLocImg);
		Button send = (Button) activity.findViewById(R.id.addComment);
		ImageButton attachment = (ImageButton) activity.findViewById(R.id.attachmentBtn);
		attachment.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				displayAttchmentView();
			}

		});
		send.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				if(rateComment.getText() != null && !"".equals(rateComment.getText().toString())){
					addComment(complaint.getId(), rateComment.getText().toString());
				}else{
					showEmptyCommentTextMessage();
				}
			}

		});
		getCurrentLocation();
	}

	private void displayAttchmentView(){
		mView.displayAttachmentDetailsView(this, complaint);
	}
	private void getCurrentLocation() {

		try {
			Display display = activity.getWindowManager().getDefaultDisplay();
			if ("".equals(GPSLocationManager.MAP_SIZE)) {
				@SuppressWarnings("deprecation")
				int width = display.getWidth();
				Log.i("Screen width :", String.valueOf(width));
				GPSLocationManager.MAP_SIZE = width + "x"
						+ (int) GenericUtils.dipToPixels(activity, 150);
			}
			OrderCoordinator.handleOrder(this, RequestFactory
					.createMapAPIRequest(complaint.getLatitude(),
							complaint.getLongitude(),
							GPSLocationManager.DEFAULT_ZONE,
							GPSLocationManager.MAP_SIZE));
		} catch (Exception ex) {

		}
	}

	private void addComment(String rateNumber, String comment) {
		try {
			OrderCoordinator.handleOrder(this,
					RequestFactory.addCommentRequest(rateNumber, comment));
		} catch (Exception ex) {

		}
	}

	private void showError() {
		closeProgress();
		Toast.makeText(mView,
				mView.getResources().getString(R.string.connection_error),
				Toast.LENGTH_LONG).show();
	}

	private void showSucceed() {
		closeProgress();
		Toast.makeText(mView,
				mView.getResources().getString(R.string.send_ccomment_succeed),
				Toast.LENGTH_LONG).show();
	}
	
	private void showEmptyCommentTextMessage() {
		closeProgress();
		Toast.makeText(mView,
				mView.getResources().getString(R.string.empty_complaint_comment),
				Toast.LENGTH_LONG).show();
	}

	@Override
	public void preExecution() {
		// TODO Auto-generated method stub
		showProgressBar();

	}

	@Override
	public void postExecution(Response response) {
		// TODO Auto-generated method stub
		try {
			if (response.getRequestName().equals(Request.USER_LOC_ORDER)) {

				byte[] locImage = (byte[]) response.getResponseData();
				Bitmap bmp = BitmapFactory.decodeByteArray(locImage, 0,
						locImage.length);
				currentLocationImage.setVisibility(View.VISIBLE);
				currentLocationImage.setImageBitmap(bmp);
				closeProgress();
			} else if (response.getRequestName().equals(Request.ADD_COMMENT_ORDER)) {
				String status = ResponseProcessingHelper.getInstance()
						.handleAddComment(response);
				if (status != null) {
					showSucceed();
				} else {
					showError();
				}
			}
		} catch (Exception ex) {
			showError();
		}

	}

	public void showProgressBar() {
		if (progress != null) {
			progress.show();
		}
	}

	public void closeProgress() {
		if (progress != null) {
			progress.dismiss();
		}
	}
}

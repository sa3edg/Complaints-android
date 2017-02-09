package com.ebttikarat.complaints.gui.view;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.ebttikarat.complaints.client.OrderCoordinator;
import com.ebttikarat.complaints.client.RequestFactory;
import com.ebttikarat.complaints.client.common.Request;
import com.ebttikarat.complaints.client.common.Response;
import com.ebttikarat.complaints.client.processing.ResponseProcessingHelper;
import com.ebttikarat.complaints.common.model.Attachment;
import com.ebttikarat.complaints.common.model.Complaint;
import com.ebttikarat.complaints.common.model.Unit;
import com.ebttikarat.complaints.common.model.User;
import com.ebttikarat.complaints.dao.DBAdapter;
import com.ebttikarat.complaints.gui.AbstractFragment;
import com.ebttikarat.complaints.gui.CustomProgressDialog;
import com.ebttikarat.complaints.gui.R;
import com.ebttikarat.complaints.gui.model.FileBrowserItem;
import com.ebttikarat.complaints.gui.model.PhoneNumber;
import com.ebttikarat.complaints.services.GPSLocationManager;
import com.ebttikarat.complaints.util.DateUtils;
import com.ebttikarat.complaints.util.FileUtils;
import com.ebttikarat.complaints.util.GenericUtils;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class EditComplaintView extends AbstractFragment {

	private final String TAG = "ADD_COMPLAINT";
	private MainView mainView;
	private List<FileBrowserItem> attachments = new ArrayList<FileBrowserItem>();
	private TextView attachmentCount;
	private ImageView currentLocationImage;
	private CustomProgressDialog progress;
	private List<Unit> unites = new ArrayList<Unit>();
	private List<CharSequence> unitesText = new ArrayList<CharSequence>();
	private ArrayAdapter<CharSequence> adapter;
	private int fileAttachIndex = 0;
	private String complaintId = "";
	private EditText complaintTitle;
	private EditText locDescription;
	private Spinner unitesSpinner;
	private Complaint complaint;
	private Bitmap locationBitmap;
	private String phoneNumber;
	private String accessToken;
	private EditText phoneNumberText;
	private EditText activationNumber;

	public EditComplaintView(MainView mView, Complaint complaint) {
		super(mView);
		this.setMainView(mView);
		this.complaint = complaint;
		progress = new CustomProgressDialog(mView);
		for(final Attachment att : complaint.getFiles()){
			attachments.add(att.getFileBrowserItem());
		}
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.add_new_complaint_layout,
				container, false);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedState) {
		super.onActivityCreated(savedState);
		unitesSpinner = (Spinner) activity.findViewById(R.id.directionsList);
//		 adapter = ArrayAdapter.createFromResource(
//		 activity, R.array.areas_arrays,
//		 R.layout.arabic_spinner_textview);
		adapter = new ArrayAdapter<CharSequence>(activity,
				R.layout.arabic_spinner_textview, unitesText);
		// adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		adapter.setDropDownViewResource(R.layout.arabic_spinner_textview);
		unitesSpinner.setAdapter(adapter);
		
		setAttachmentCount((TextView) activity
				.findViewById(R.id.attachmentCount));
		getAttachmentCount().setText(String.valueOf(getAttachments().size()));

		ImageButton browse = (ImageButton) activity
				.findViewById(R.id.browseBtn);
		// unitesSpinner.setOnItemSelectedListener(new MyOnItemSelectedListener());
		complaintTitle = (EditText) activity
				.findViewById(R.id.complaintTitleTxt);
		complaintTitle.setText(complaint.getTitle());
		locDescription = (EditText) activity
				.findViewById(R.id.locationDescTxt);
		locDescription.setText(complaint.getLocationDescription());
		browse.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				showFIleBrowser();
			}

		});

		currentLocationImage = (ImageView) activity
				.findViewById(R.id.currentLocImg);
		ImageButton capturePhoto = (ImageButton) activity
				.findViewById(R.id.takePicBtn);

		capturePhoto.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				showCameraView(false);
			}

		});

		ImageButton captureVideo = (ImageButton) activity
				.findViewById(R.id.takeVideoBtn);

		captureVideo.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				showCameraView(true);
			}

		});

		ImageButton attachmentBtn = (ImageButton) activity
				.findViewById(R.id.attachmentBtn);

		attachmentBtn.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				showAttachmentView();
			}

		});

		Button send = (Button) activity.findViewById(R.id.sendBtn);

		send.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				if ((complaintTitle.getText().toString() == null || ""
						.equals(complaintTitle.getText().toString()))) {
					showEmptyTitleMsg();
				} else {
				sendComplaint();
				}
			}

		});
		Button save = (Button) activity.findViewById(R.id.saveBtn);
		save.setText(mainView.getResources().getString(R.string.edit_complaint_save_btn));
		save.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				showConfirmationDialog();
			}

		});
		// get current location
		if(unitesText.size() == 0){
			if (User.destinationUnitMap.size() > 0) {
				for (final Unit un : User.destinationUnitMap.values()) {
					unitesText.add(un.getName());
					unites.add(un);
				}
				int i = 0;
				int index = 0;
				for (final CharSequence unName : unitesText) {
					if(unName.toString().equals(complaint.getDestinationUnit())){
						index = i;
						break;
					}
					i++;
				}
				adapter.notifyDataSetChanged();
				unitesSpinner.setSelection(index);
			} else {
				getUnites();
			}
			getCurrentLocation();
		}else{
			
			currentLocationImage.setVisibility(View.VISIBLE);
			if(getLocationBitmap() != null){
				currentLocationImage.setImageBitmap(getLocationBitmap());
			}
		}
		

	}

	private void showEmptyTitleMsg() {
		Toast.makeText(
				mainView,
				mainView.getResources().getString(
						R.string.empty_complaint_title), Toast.LENGTH_LONG)
				.show();
	}
	private void showAttachmentView(){
		mainView.displayAttachmentView(mainView.getCurrentAddComplaint(), true);
	}

	private void showFIleBrowser(){
		mainView.displayFileBrowser(mainView.getCurrentAddComplaint(), false, true);
	}
	private void showCameraView(boolean isVideo){
		mainView.displayCameraViewInEditMode(mainView.getCurrentEditComplaint(), null, isVideo);
	}
	private void sendComplaint() {
		if (User.mySetting.getDefaultMobileNumber() == null) {
			showRegDialog();
		} else if (User.mySetting.getDefaultMobileNumber() != null
				&& User.mySetting.getDefaultMobileNumber().getIsVerified()
						.equals("0")) {
			showActivationDialog();
		} else {
		if(GenericUtils.isInternetConnected(mainView)){
		Complaint complaint = new Complaint();
		Unit uni = unites.get(unitesSpinner.getSelectedItemPosition());
		complaint.setTitle(complaintTitle.getText().toString());
		complaint.setDestinationUnit(uni.getId());
		complaint.setLocationDescription(locDescription.getText().toString());
		complaint.setLatitude(GenericUtils.roundFourDecimals(GPSLocationManager.latitude));
		complaint.setLongitude(GenericUtils.roundFourDecimals(GPSLocationManager.longitude));
		try {
			OrderCoordinator.handleOrder(this,
					RequestFactory.addComplaintRequest(complaint));
		} catch (Exception ex) {
			Log.d(TAG, ex.getMessage());
		}}else{
			showSaveComplaintMsg();
		}
		}
	}
	private void showRegDialog() {
		final Dialog mplayerDialog = new Dialog(mainView);
		mplayerDialog.setCancelable(true);
		mplayerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// dialog.getWindow().getDecorView()
		// .setBackgroundResource(android.R.color.transparent);
		mplayerDialog.setContentView(R.layout.registeration_dialog_layout);

		phoneNumberText = (EditText) mplayerDialog
				.findViewById(R.id.mobileNumber);

		final Button ok = (Button) mplayerDialog.findViewById(R.id.okBtn);
		// Set On ClickListener

		ok.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				final String pNumber = phoneNumberText.getText().toString();
				// TODO Auto-generated method stub
				if (pNumber != null && !"".equals(pNumber)) {
					phoneNumber = pNumber;
					mplayerDialog.dismiss();
					registerNumber(pNumber);
				}
			}
		});
		final Button cancel = (Button) mplayerDialog
				.findViewById(R.id.cancelBtn);
		// Set On ClickListener
		cancel.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				mplayerDialog.dismiss();
			}
		});
		mplayerDialog.show();

	}

	private void showActivationDialog() {
		final Dialog mplayerDialog = new Dialog(mainView);
		mplayerDialog.setCancelable(true);
		mplayerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// dialog.getWindow().getDecorView()
		// .setBackgroundResource(android.R.color.transparent);
		mplayerDialog.setContentView(R.layout.activation_dialog_layout);

		activationNumber = (EditText) mplayerDialog
				.findViewById(R.id.numberTxt);

		final Button ok = (Button) mplayerDialog.findViewById(R.id.okBtn);
		// Set On ClickListener
		ok.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				final String pNumber = activationNumber.getText().toString();
				// TODO Auto-generated method stub
				if (pNumber != null && !"".equals(pNumber)) {
					accessToken = pNumber;
					mplayerDialog.dismiss();
					activateNumber(EditComplaintView.this.phoneNumber,
							pNumber);
				}
			}
		});
		final Button cancel = (Button) mplayerDialog
				.findViewById(R.id.cancelBtn);
		// Set On ClickListener
		cancel.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				mplayerDialog.dismiss();
			}
		});
		mplayerDialog.show();

	}
	private void registerNumber(String number) {
		try {

			OrderCoordinator.handleOrder(this,
					RequestFactory.registerMobileNumber(number));
		} catch (Exception ex) {

		}
	}

	private void activateNumber(String mNumber, String aCode) {
		try {
			String mNum = "";
			if (mNumber == null || "".equals("")) {
				if (User.mySetting.getDefaultMobileNumber() != null
						&& User.mySetting.getDefaultMobileNumber()
								.getIsVerified().equals("0")) {
					mNum = User.mySetting.getDefaultMobileNumber()
							.getPhoneNumber();
				}
			} else {
				mNum = mNumber;
			}
			OrderCoordinator.handleOrder(this,
					RequestFactory.verifyMobileNumber(mNum, aCode));
		} catch (Exception ex) {

		}
	}
	private void showSaveComplaintMsg(){
		closeProgress();
		Toast.makeText(mainView, mainView.getResources().getString(R.string.save_complaint_msg), Toast.LENGTH_LONG).show();
	}
	
	private void showConfirmationDialog(){
		final Dialog mplayerDialog = new Dialog(mainView);
		mplayerDialog.setCancelable(true);
		mplayerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//		dialog.getWindow().getDecorView()
//				.setBackgroundResource(android.R.color.transparent);
		mplayerDialog.setContentView(R.layout.confirmation_dialog_layout);
	
		TextView title = (TextView) mplayerDialog
				.findViewById(R.id.confirmationTitle);
		title.setText(mainView.getResources().getString(R.string.save_complaint_dialog_title));
		TextView body = (TextView) mplayerDialog
				.findViewById(R.id.confirmationBody);
		body.setText(mainView.getResources().getString(R.string.save_complaint_dialog_body));
		final Button ok = (Button) mplayerDialog.findViewById(R.id.okBtn);
		// Set On ClickListener
		ok.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				mplayerDialog.dismiss();
				saveComplaint();
			}
		});
		final Button cancel = (Button) mplayerDialog.findViewById(R.id.cancelBtn);
		// Set On ClickListener
		cancel.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				mplayerDialog.dismiss();
			}
		});
		mplayerDialog.show();
		
	}
	
	private void saveComplaint() {
		Complaint complaint = new Complaint();
		Unit uni = unites.get(unitesSpinner.getSelectedItemPosition());
		complaint.setTitle(complaintTitle.getText().toString());
		complaint.setDestinationUnit(uni.getId());
		complaint.setLocationDescription(locDescription.getText().toString());
		complaint.setLatitude(GPSLocationManager.latitude);
		complaint.setLongitude(GPSLocationManager.longitude);
		complaint.setId(this.complaint.getId());
		complaint.setLastUpdateDate(DateUtils.getLastUpdateDate());
		setAttachments(mainView.getCurrentEditComplaint().getAttachments());
		for(final FileBrowserItem file : getAttachments()){
			Attachment attachment = new Attachment();
			attachment.setFileId(UUID.randomUUID().toString());
			attachment.setComplaintId(this.complaint.getId());
			attachment.setFileData(file.getFileData());
			attachment.setFileName(file.getName());
			attachment.setFilePath(file.getPath());
			attachment.setFileType(FileUtils.getFileType(file.getName()));
			complaint.getFiles().add(attachment);
		}
		try {
			DBAdapter.updateComplaintWIthAttachment(complaint);
			Toast.makeText(mainView, mainView.getResources().getString(R.string.saved_succeed), Toast.LENGTH_LONG).show();
		} catch (Exception ex) {
			Log.i(TAG, ex.getMessage());
		}
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
					.createMapAPIRequest(GPSLocationManager.latitude,
							GPSLocationManager.longitude,
							GPSLocationManager.DEFAULT_ZONE,
							GPSLocationManager.MAP_SIZE));
		} catch (Exception ex) {

		}
	}

	private void getUnites() {

		try {

			OrderCoordinator
					.handleOrder(this, RequestFactory.getUnitsRequest());
		} catch (Exception ex) {

		}
	}
	private void attachFile(String complaintId, int index) {

		try {
			FileBrowserItem file = attachments.get(index);
			OrderCoordinator
					.handleOrder(this, RequestFactory.uploadFileRequest(complaintId, file.getPath()));
		} catch (Exception ex) {

		}
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
				// closeProgress();
				byte[] locImage = (byte[]) response.getResponseData();
				Bitmap bmp = BitmapFactory.decodeByteArray(locImage, 0,
						locImage.length);
				currentLocationImage.setVisibility(View.VISIBLE);
				setLocationBitmap(bmp);
				closeProgress();
				currentLocationImage.setImageBitmap(bmp);
				// get destination unites
//				if(User.destinationUnitMap.size() > 0){
//						for (final Unit un : User.destinationUnitMap.values()) {
//							unitesText.add(un.getName());
//							unites.add(un);
//						}
//						int i = 0;
//						int index = 0;
//						for (final CharSequence unName : unitesText) {
//							if(unName.toString().equals(complaint.getDestinationUnit())){
//								index = i;
//								break;
//							}
//							i++;
//						}
//						adapter.notifyDataSetChanged();
//						unitesSpinner.setSelection(index);
//						closeProgress();
//				}else{
//					getUnites();
//				}
			} else if (response.getRequestName()
					.equals(Request.GET_UNITS_ORDER)) {
				unites = ResponseProcessingHelper.getInstance()
						.handleGetUnitsResponse(response);
				if (unites != null) {
					for (final Unit un : unites) {
						unitesText.add(un.getName());
						if(!User.destinationUnitMap.containsKey(un.getId())){
							User.destinationUnitMap.put(un.getId(), un);
						}
					}
					int i = 0;
					int index = 0;
					for (final CharSequence unName : unitesText) {
						if(unName.toString().equals(complaint.getDestinationUnit())){
							index = i;
							break;
						}
						i++;
					}
					adapter.notifyDataSetChanged();
					unitesSpinner.setSelection(index);
					closeProgress();
				}else{
					showError();
				}
				
			} else if (response.getRequestName().equals(
					Request.ATTACH_FILE_ORDER)) {
				String fileId = ResponseProcessingHelper.getInstance().handleAttachFileResponse(response);
				if(fileId != null){
					fileAttachIndex++;
					if(fileAttachIndex < getAttachments().size()){
						attachFile(complaintId, fileAttachIndex);
					}else{
						closeProgress();
						DBAdapter.deleteComplaint(complaint);
						DBAdapter.deleteComplaintAttachment(complaint);
						Toast.makeText(mainView, mainView.getResources().getString(R.string.sent_succeed), Toast.LENGTH_LONG).show();
					}
				}else{
					showError();
				}

			} else if (response.getRequestName().equals(
					Request.ADD_COMPLAINT_ORDER)) {
				String complaintId = ResponseProcessingHelper.getInstance().handleAddComplaintResponse(response);
				this.complaintId = complaintId;
				if(this.complaintId != null){
					if(getAttachments().size() > 0){
						attachFile(complaintId, fileAttachIndex);
					}else if(this.complaintId != null){
						closeProgress();
						DBAdapter.deleteComplaint(complaint);
						DBAdapter.deleteComplaintAttachment(complaint);
						Toast.makeText(mainView, mainView.getResources().getString(R.string.sent_succeed), Toast.LENGTH_LONG).show();
					}
				}else{
					showError();
				}
			}else if (response.getRequestName().equals(
					Request.REGISTER_PHONE_ORDER)) {
				String status = ResponseProcessingHelper.getInstance()
						.handlePhoneRegistrationResponse(response);

				if (status != null && phoneNumber != null) {
					PhoneNumber num = new PhoneNumber();
					num.setPhoneNumber(phoneNumber);
					num.setIsPrefered("1");
					User.mySetting.setDefaultMobileNumber(num);
					DBAdapter.addPhoneNumber(num);
					closeProgress();
					showActivationDialog();
				} else {
					showError();
				}

			} else if (response.getRequestName().equals(
					Request.VERIFY_PHONE_ORDER)) {
				String status = ResponseProcessingHelper.getInstance()
						.handlePhoneVerificationResponse(response);
				closeProgress();
				if (status != null && "true".equals(status)) {
					PhoneNumber num = new PhoneNumber();
					num.setPhoneNumber(phoneNumber);
					num.setAccessToken(accessToken);
					num.setIsPrefered("1");
					num.setIsVerified("1");
					User.mySetting.setDefaultMobileNumber(num);
					DBAdapter.updatePhoneNumber(num);
					closeProgress();
					sendComplaint();
				} else {
					showError();
				}
			}
			
		} catch (Exception ex) {
			showError();
		}

	}

	private void showError(){
		closeProgress();
		Toast.makeText(mainView, mainView.getResources().getString(R.string.connection_error), Toast.LENGTH_LONG).show();
	}
	public MainView getMainView() {
		return mainView;
	}

	public void setMainView(MainView mainView) {
		this.mainView = mainView;
	}

	public List<FileBrowserItem> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<FileBrowserItem> attachments) {
		this.attachments = attachments;
	}

	public TextView getAttachmentCount() {
		return attachmentCount;
	}

	public void setAttachmentCount(TextView attachmentCount) {
		this.attachmentCount = attachmentCount;
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

	public Bitmap getLocationBitmap() {
		return locationBitmap;
	}

	public void setLocationBitmap(Bitmap locationBitmap) {
		this.locationBitmap = locationBitmap;
	}
}

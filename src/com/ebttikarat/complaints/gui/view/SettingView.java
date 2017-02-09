package com.ebttikarat.complaints.gui.view;



import com.ebttikarat.complaints.client.OrderCoordinator;
import com.ebttikarat.complaints.client.RequestFactory;
import com.ebttikarat.complaints.client.common.Request;
import com.ebttikarat.complaints.client.common.Response;
import com.ebttikarat.complaints.client.processing.ResponseProcessingHelper;
import com.ebttikarat.complaints.common.model.User;
import com.ebttikarat.complaints.dao.DBAdapter;
import com.ebttikarat.complaints.gui.AbstractFragment;
import com.ebttikarat.complaints.gui.CustomProgressDialog;
import com.ebttikarat.complaints.gui.R;
import com.ebttikarat.complaints.gui.model.PhoneNumber;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SettingView extends AbstractFragment {

    private MainView mView;
    private String phoneNumber;
	private String accessToken;
	private EditText phoneNumberText;
	private EditText activationNumber;
	private CustomProgressDialog progress;
	public SettingView(MainView mView) {
		super(mView);
		this.mView = mView;
		progress = new CustomProgressDialog(mView);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.favourite_number_layout,
				container, false);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedState) {
		super.onActivityCreated(savedState);
		// Create grid view
		
		phoneNumberText = (EditText) activity
				.findViewById(R.id.numberTxt);
		Button save = (Button) activity
				.findViewById(R.id.saveBtn);
		phoneNumberText.setText(User.mySetting.getDefaultMobileNumber().getPhoneNumber());
		
		save.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				final String mNumber = phoneNumberText.getText().toString();
				if(mNumber != null && !"".equals(mNumber)){
					SettingView.this.phoneNumber = mNumber;
					if(mNumber.length() < 10 || mNumber.length() > 12){
						showValidationError();
					}else{
						registerNumber(mNumber);
					}
				}
			}

		});
		
		
       
	}

	private void showActivationDialog(){
		final Dialog mplayerDialog = new Dialog(mView);
		mplayerDialog.setCancelable(true);
		mplayerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//		dialog.getWindow().getDecorView()
//				.setBackgroundResource(android.R.color.transparent);
		mplayerDialog.setContentView(R.layout.activation_dialog_layout);
	
		activationNumber = (EditText) mplayerDialog
				.findViewById(R.id.numberTxt);
		
		final Button ok = (Button) mplayerDialog.findViewById(R.id.okBtn);
		// Set On ClickListener
		ok.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				final String pNumber = activationNumber.getText().toString();
				// TODO Auto-generated method stub
				if(pNumber != null && !"".equals(pNumber)){
					accessToken = pNumber;
					mplayerDialog.dismiss();
					activateNumber(SettingView.this.phoneNumber, pNumber);
				}
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
	
	private void registerNumber(String number){
		try {

			OrderCoordinator
					.handleOrder(this, RequestFactory.registerMobileNumber(number));
		} catch (Exception ex) {

		}
	}
	
    private void activateNumber(String mNumber,String aCode){
    	try {
			OrderCoordinator
					.handleOrder(this, RequestFactory.verifyMobileNumber(mNumber, aCode));
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
		try{
			if (response.getRequestName().equals(
					Request.REGISTER_PHONE_ORDER)) {
				String status = ResponseProcessingHelper.getInstance().handlePhoneRegistrationResponse(response);
				
				if(status != null && phoneNumber != null){
					
					closeProgress();
					showActivationDialog();
				}else{
					showError();
				}
				
			}else if (response.getRequestName().equals(
					Request.VERIFY_PHONE_ORDER)) {
				String status = ResponseProcessingHelper.getInstance().handlePhoneVerificationResponse(response);
				//closeProgress();
				if(status != null && "true".equals(status)){
					PhoneNumber num = new PhoneNumber();
					num.setPhoneNumber(phoneNumber);
					num.setAccessToken(accessToken);
					num.setIsPrefered("1");
					num.setIsVerified("1");
					User.mySetting.setDefaultMobileNumber(num);
					DBAdapter.setPreferedNumber(num);
					DBAdapter.addPhoneNumber(num);
					showActivationSucced();
				}else{
					showError();
				}
			}
			
		} catch (Exception ex) {
			showError();
		}
	}
	private void showValidationError(){
		closeProgress();
		Toast.makeText(mView, mView.getResources().getString(R.string.validation_error), Toast.LENGTH_LONG).show();
	}
	private void showError(){
		closeProgress();
		Toast.makeText(mView, mView.getResources().getString(R.string.connection_error), Toast.LENGTH_LONG).show();
	}
	private void showActivationSucced(){
		closeProgress();
		Toast.makeText(mView, mView.getResources().getString(R.string.activation_succeed), Toast.LENGTH_LONG).show();
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

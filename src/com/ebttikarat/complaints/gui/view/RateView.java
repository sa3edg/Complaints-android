package com.ebttikarat.complaints.gui.view;

import com.ebttikarat.complaints.client.OrderCoordinator;
import com.ebttikarat.complaints.client.RequestFactory;
import com.ebttikarat.complaints.client.common.Response;
import com.ebttikarat.complaints.client.processing.ResponseProcessingHelper;
import com.ebttikarat.complaints.gui.AbstractFragment;
import com.ebttikarat.complaints.gui.CustomProgressDialog;
import com.ebttikarat.complaints.gui.R;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

public class RateView extends AbstractFragment {

	private MainView mView;
	private CustomProgressDialog progress;
	public RateView(MainView mView) {
		super(mView);
		this.mView = mView;
		progress = new CustomProgressDialog(mView);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.rate_layout,
				container, false);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedState) {
		super.onActivityCreated(savedState);
		Button send = (Button) activity
				.findViewById(R.id.sendRate);
		final RatingBar ratingBar = (RatingBar) activity
				.findViewById(R.id.ratingBar);
		final EditText rateComment = (EditText) activity
				.findViewById(R.id.RateComment);

		send.setOnClickListener(new OnClickListener() {
			public void onClick(View arg0) {
				doRate(String.valueOf((int)ratingBar.getRating()), rateComment.getText().toString());
			}

		});
       
		
       
	}
	
	private void doRate(String rateNumber, String comment){
		try {
			OrderCoordinator
					.handleOrder(this, RequestFactory.addRateRequest(rateNumber, comment));
		} catch (Exception ex) {

		}
	}

	private void showError(){
		closeProgress();
		Toast.makeText(mView, mView.getResources().getString(R.string.connection_error), Toast.LENGTH_LONG).show();
	}
	
	private void showSucceed(){
		closeProgress();
		Toast.makeText(mView, mView.getResources().getString(R.string.rating_succeed), Toast.LENGTH_LONG).show();
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
			String status = ResponseProcessingHelper.getInstance().handleRateResponse(response);
			if(status != null){
				showSucceed();
			}else{
				showError();
			}
		}catch(Exception ex){
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

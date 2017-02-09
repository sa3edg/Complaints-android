package com.ebttikarat.complaints.gui;

import com.ebttikarat.complaints.client.common.Response;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.Window;


public abstract class AbstractScreen implements IActivity {

	protected Response response;
	protected FragmentActivity activity;
	private CustomProgressDialog progress;

	public AbstractScreen(FragmentActivity activity) {
		this.activity = activity;
	}

	public abstract void loadScreen();

	@Override
	public void preExecution() {
		// TODO Auto-generated method stub

	}

	@Override
	public void postExecution(Response response) {
		// TODO Auto-generated method stub

	}

	public void showProgressBar() {
		progress = new CustomProgressDialog(activity);
		progress.show();
	}

	
	public void closeProgress() {
		if(progress != null)
		{
		   progress.dismiss();
		}
	}

	public FragmentActivity getActivity() {
		return activity;
	}
	
	private class CustomProgressDialog extends Dialog {

		public CustomProgressDialog(Context context) {
			super(context);
			loadScreen();
		}


		/**
		 * Initializes wheel
		 * 
		 * @param id
		 *            the wheel widget Id
		 */
		public void loadScreen() {
			// TODO Auto-generated method stub
			requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.progress);
	        setCancelable(false);
	        getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
	       
		}

	}
	
	

}

package com.ebttikarat.complaints.gui;

import com.ebttikarat.complaints.client.common.Response;

import android.app.Activity;
import android.support.v4.app.Fragment;

public abstract class AbstractFragment extends Fragment implements IActivity{

	protected Activity activity;
//	private static CustomProgressDialog progress;
	public AbstractFragment(Activity activity) {
		this.activity = activity;
	}
	@Override
	public void preExecution() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postExecution(Response response) {
		// TODO Auto-generated method stub
		
	}
//	public void showProgressBar() {
//		progress = new CustomProgressDialog(activity);
//		progress.show();
//	}
//
//	
//	public void closeProgress() {
//		if(progress != null)
//		{
//		   progress.dismiss();
//		   progress.cancel();
//		   progress = null;
//		}
//	}
	
}

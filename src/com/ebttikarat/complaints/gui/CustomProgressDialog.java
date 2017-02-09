package com.ebttikarat.complaints.gui;

import android.app.Dialog;
import android.content.Context;
import android.view.Window;

public class CustomProgressDialog extends Dialog {

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
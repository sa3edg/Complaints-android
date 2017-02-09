package com.ebttikarat.complaints.util;

import java.text.DecimalFormat;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.util.TypedValue;

public class GenericUtils {

	public static float dipToPixels(Context context, float dipValue) {
	    DisplayMetrics metrics = context.getResources().getDisplayMetrics();
	    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dipValue, metrics);
	}
	public static String roundFourDecimals(String input)
	{
		if(input == null || "".equals(input)){
			return "";
		}
		double digits = Double.parseDouble(input);
	    DecimalFormat twoDForm = new DecimalFormat("##.####");
	    return String.valueOf(Double.valueOf(twoDForm.format(digits)));
	}
	
	public static boolean isInternetConnected(Context context){
		ConnectivityManager cm =
		        (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
	}
}

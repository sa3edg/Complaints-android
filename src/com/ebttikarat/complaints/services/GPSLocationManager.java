package com.ebttikarat.complaints.services;



import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

public class GPSLocationManager {
	private static GPSLocationManager self = null;
	private Activity activity;
	public static long TRACKING_INTERVAL = 1*60*1000;
	private static float TRACKING_DISTANCE = 10F;
	public static String latitude = "";
	public static String longitude = "";
	public static final String DEFAULT_ZONE = "14";
	public static String MAP_SIZE = "";
	
	private GPSLocationManager(Activity activity)
	{
		this.activity = activity;
	}
	public static GPSLocationManager getInstance(Activity activity)
	{
		if(self == null)
		{
			
			self = new GPSLocationManager(activity);
		}
		return self;
	}
	public void startTracking()
	{
		LocationManager lm = (LocationManager)activity.getSystemService(Context.LOCATION_SERVICE); 
		Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
		
		if(location != null) {
			updateLocation(location);
		}
		
		final LocationListener locationListener = new LocationListener() {
		    public void onLocationChanged(Location location) {
		    	updateLocation(location);
		    }

		    public void onProviderDisabled(String arg0) {
		        // TODO Auto-generated method stub

		    }

		    public void onProviderEnabled(String arg0) {
		        // TODO Auto-generated method stub

		    }

		    public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
		        // TODO Auto-generated method stub

		    }

			
		};

		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, TRACKING_INTERVAL, TRACKING_DISTANCE, locationListener);
	}
	
	private void updateLocation(Location location)
	{
	    latitude = String.valueOf(location.getLatitude());
	    longitude = String.valueOf(location.getLongitude());
	}

}

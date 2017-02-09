package com.ebttikarat.complaints.client;

import android.util.Log;

import com.ebttikarat.complaints.client.common.Request;
import com.ebttikarat.complaints.client.processing.IProcessor;
import com.ebttikarat.complaints.client.processing.ProcessorFactory;
import com.ebttikarat.complaints.gui.IActivity;

public class OrderCoordinator {
	
	private static OrderCoordinator self = null;
	private OrderCoordinator()
	{
		
	}
	
	public static void createInstance()
	{
		if(self == null)
		{
			self = new OrderCoordinator();
		}
	}
	public static void handleOrder(IActivity activity,Request request)
	{
		IProcessor processor = null;
		try
		{
			
			processor = ProcessorFactory.create(activity,request);
			processor.preprocess();
			processor.process();
		}
		catch(Exception ex)
		{
			Log.i("Error", ex.getMessage());
		}
		finally
		{
			if(processor != null)
				processor.terminate();
		}
	}

}

package com.ebttikarat.complaints.util;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.widget.ImageView;

public class ImageUtils {

	public static void displayImage(String imageData, ImageView imageView)
    {
		byte[] data = Base64.decode(imageData, Base64.DEFAULT);
		Bitmap bmp=BitmapFactory.decodeByteArray(data, 0, data.length);
        if(bmp!=null)
        {
            imageView.setImageBitmap(bmp);
        }
    }
	public static byte[] getBitmapAsByteArray(Bitmap bitmap) {
	    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	    bitmap.compress(CompressFormat.PNG, 0, outputStream);       
	    return outputStream.toByteArray();
	}
}

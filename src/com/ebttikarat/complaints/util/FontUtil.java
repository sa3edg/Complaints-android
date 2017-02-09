package com.ebttikarat.complaints.util;

import java.util.Hashtable;

import android.content.Context;
import android.graphics.Typeface;

public class FontUtil {
	private static final Hashtable<String, Typeface> cache = new Hashtable<String, Typeface>();
	public static final String BLACK_FONT = "f_black";
	public static final String BOLD_FONT = "f_bold";
	public static final String MEDUIM_FONT = "f_medium";
	public static final String LIGHT_FONT = "f_light";
	public static Typeface get(Context c, String name) {
		synchronized (cache) {
			if (!cache.containsKey(name)) {
				Typeface t = Typeface.createFromAsset(c.getAssets(),
						String.format("fonts/%s.otf", name));
				cache.put(name, t);
			}
			return cache.get(name);
		}
	}
}

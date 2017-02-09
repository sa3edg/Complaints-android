package com.ebttikarat.complaints.util;

import java.util.List;

import org.apache.http.NameValuePair;

public class URLEncodingUtil {

	public static  String prepareParameters(List<NameValuePair> parms)
    {
        if(parms == null)
            return "";
        String parameters = "";
        int i = 0;
        for(NameValuePair parm : parms)
        {
            if(i != parms.size()-1)
            {
               parameters += parm.getName()+"="+parm.getValue()+"&";
            }
            else
            {
                parameters += parm.getName()+"="+parm.getValue();
            }
            i++;
        }
        return parameters;
    }
}

package com.ebttikarat.complaints.gui.adapter;

import java.util.List;

import com.ebttikarat.complaints.gui.R;
import com.ebttikarat.complaints.gui.model.NavDrawerItem;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NavDrawerListAdapter extends ArrayAdapter<NavDrawerItem> {

    private final Context context;

    List<NavDrawerItem> items;
    static class ViewHolder { 
    	TextView text;
    	public ImageView image;
    }

    public NavDrawerListAdapter(Context context, List<NavDrawerItem> objects) {
        super(context, R.layout.drawer_list_item, objects);
        this.context = context;
        this.items = objects;      
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	NavDrawerItem model = (NavDrawerItem)getItem(position);
    	View rowView = convertView;
    	ViewHolder viewHolder;
    	if (rowView == null) {
    	      LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	      rowView = inflater.inflate(R.layout.drawer_list_item, null);
    	      viewHolder = new ViewHolder();
    	      viewHolder.text = (TextView) rowView.findViewById(R.id.title);
    	      viewHolder.image = (ImageView) rowView.findViewById(R.id.icon);
    	      rowView.setTag(viewHolder);
    		} 
    		else { 
    			viewHolder = (ViewHolder) rowView.getTag(); 
        } 
    	    	       
        viewHolder.text.setText(model.getTitle());        
        viewHolder.image.setImageResource(model.getIcon());
        return rowView;       
    }

}
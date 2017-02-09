package com.ebttikarat.complaints.gui.adapter;

import java.util.List;

import com.ebttikarat.complaints.gui.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import android.app.Activity;

public class MyComplaintsAdapter<Complaint> extends ArrayAdapter<Complaint> {

	int resource;
	String response;
	Context context;

	// Initialize adapter
	public MyComplaintsAdapter(Context context, int resource,
			List<Complaint> items) {
		super(context, resource, items);
		this.resource = resource;
		this.context = context;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Get the current alert object
		com.ebttikarat.complaints.common.model.Complaint model = (com.ebttikarat.complaints.common.model.Complaint) getItem(position);

		View row = convertView;
		ViewHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(resource, parent, false);

			holder = new ViewHolder();
			holder.number = (TextView) row.findViewById(R.id.complaintNumber);
			holder.title = (TextView) row.findViewById(R.id.complaintTitle);
			holder.lastUpdate = (TextView) row
					.findViewById(R.id.complaintLastUpdate);
			row.setTag(holder);
		} else {
			holder = (ViewHolder) row.getTag();
		}
		
		if (model.getId() != null) {
			holder.number.setText(context.getResources().getString(R.string.my_complaint_number_txt) +" : "+ model.getId().trim());
		}
		if (model.getTitle() != null) {
			holder.title.setText(model.getTitle().trim());
		}
		if (model.getLastUpdateDate() != null) {
			holder.lastUpdate.setText(model.getLastUpdateDate().trim());
		}

		return row;
	}

	static class ViewHolder {
		TextView number;
		TextView title;
		TextView lastUpdate;
	}

}

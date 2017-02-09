package com.ebttikarat.complaints.gui.adapter;

import java.util.List;

import com.ebttikarat.complaints.dao.DBAdapter;
import com.ebttikarat.complaints.gui.R;
import com.ebttikarat.complaints.gui.view.MainView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import android.app.Activity;
import android.app.Dialog;

public class SavedComplaintsAdapter<Complaint> extends ArrayAdapter<Complaint> {

	int resource;
	private List<Complaint> complaints;
	private MainView mView;

	// Initialize adapter
	public SavedComplaintsAdapter(MainView context, int resource, List<Complaint> items) {
		super(context, resource, items);
		this.resource = resource;
		this.mView = context;
		this.complaints = items;

	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// Get the current alert object
		final com.ebttikarat.complaints.common.model.Complaint model = (com.ebttikarat.complaints.common.model.Complaint)getItem(position);

		View row = convertView;
		ViewHolder holder = null;
       
        if(row == null)
        {
            LayoutInflater inflater = ((Activity)mView).getLayoutInflater();
            row = inflater.inflate(resource, parent, false);
           
            holder = new ViewHolder();
            holder.title = (TextView) row.findViewById(R.id.complaintTitle);
            holder.lastUpdate = (TextView) row.findViewById(R.id.complaintLastUpdate);
            holder.attachment = (ImageButton) row.findViewById(R.id.attachmentBtn);
            holder.delete = (ImageButton) row.findViewById(R.id.deleteBtn);
            holder.edit = (ImageButton) row.findViewById(R.id.editBtn);
            row.setTag(holder);
        }
        else
        {
            holder = (ViewHolder)row.getTag();
        }
        holder.title.setText(model.getTitle().trim());
        holder.lastUpdate.setText(mView.getResources().getString(R.string.last_update_date) +" : "+model.getLastUpdateDate().trim());
        final int index = position;
        holder.delete.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				showConfirmationDialog(index, model);
			}

		});
        holder.edit.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				model.setFiles(DBAdapter.getComplaintAttachment(model.getId()));
				mView.displayEditComplaintView(model);
			}

		});
        holder.attachment.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				
			}

		});
        return row;
	}
	
	private void showConfirmationDialog(final int index, final com.ebttikarat.complaints.common.model.Complaint model){
		final Dialog mplayerDialog = new Dialog(mView);
		mplayerDialog.setCancelable(true);
		mplayerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//		dialog.getWindow().getDecorView()
//				.setBackgroundResource(android.R.color.transparent);
		mplayerDialog.setContentView(R.layout.confirmation_dialog_layout);
	
		TextView title = (TextView) mplayerDialog
				.findViewById(R.id.confirmationTitle);
		title.setText(mView.getResources().getString(R.string.delete_complaint_title));
		TextView body = (TextView) mplayerDialog
				.findViewById(R.id.confirmationBody);
		body.setText(mView.getResources().getString(R.string.delete_complaint_alert));
		final Button ok = (Button) mplayerDialog.findViewById(R.id.okBtn);
		// Set On ClickListener
		ok.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				try{
					DBAdapter.deleteComplaint(model);
					DBAdapter.deleteComplaintAttachment(model);
				}catch(Exception ex){
					
				}finally{
					complaints.remove(index);
					notifyDataSetChanged();
				}
				mplayerDialog.dismiss();
				
			}
		});
		final Button cancel = (Button) mplayerDialog.findViewById(R.id.cancelBtn);
		// Set On ClickListener
		cancel.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				mplayerDialog.dismiss();
			}
		});
		mplayerDialog.show();
		
	}
	
	static class ViewHolder {
		  TextView title;
		  TextView lastUpdate;
		  ImageButton attachment;
		  ImageButton delete;
		  ImageButton edit;
		}

}

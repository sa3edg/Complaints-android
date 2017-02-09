package com.ebttikarat.complaints.gui.view;



import com.ebttikarat.complaints.client.common.Response;
import com.ebttikarat.complaints.common.model.Complaint;
import com.ebttikarat.complaints.gui.AbstractFragment;
import com.ebttikarat.complaints.gui.R;
import com.ebttikarat.complaints.gui.adapter.AttachmentDetailsListAdapter;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.GridView;
import android.widget.ImageButton;

public class AttachmentsDetailsView extends AbstractFragment {

    private MainView mView;
    private Complaint complaint;
	public AttachmentsDetailsView(MainView mView, Complaint complaint) {
		super(mView);
		this.mView = mView;
		this.complaint = complaint;
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.display_attachment_view_layout,
				container, false);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedState) {
		super.onActivityCreated(savedState);
		// Create grid view
		GridView gridView = (GridView) mView.findViewById(R.id.attachmentGridView);
		AttachmentDetailsListAdapter customGridAdapter = new AttachmentDetailsListAdapter(mView, R.layout.attachment_row_layout, complaint.getFiles());
		gridView.setAdapter(customGridAdapter);
		
		ImageButton done = (ImageButton) activity
				.findViewById(R.id.done);

		done.setOnClickListener(new OnClickListener() {

			public void onClick(View arg0) {
				showEditAcomplaint();
			}

		});
       
	}

	private void showEditAcomplaint(){
		mView.displayMyComplaintDetailsView();
	}
	@Override
	public void preExecution() {
		// TODO Auto-generated method stub

	}

	@Override
	public void postExecution(Response response) {
		// TODO Auto-generated method stub

	}
}

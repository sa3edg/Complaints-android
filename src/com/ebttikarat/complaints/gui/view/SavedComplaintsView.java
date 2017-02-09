package com.ebttikarat.complaints.gui.view;

import java.util.ArrayList;
import java.util.List;

import com.ebttikarat.complaints.common.model.Complaint;
import com.ebttikarat.complaints.dao.DBAdapter;
import com.ebttikarat.complaints.gui.R;
import com.ebttikarat.complaints.gui.adapter.SavedComplaintsAdapter;

import android.os.Bundle; 
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView; 
import android.widget.AdapterView.OnItemLongClickListener;

public class SavedComplaintsView extends ListFragment {

    private SavedComplaintsAdapter<Complaint> adapter;
    private MainView mView;
    
    private List<Complaint> savedComplaints = new ArrayList<Complaint>();
    public SavedComplaintsView(MainView context){
    	this.mView = context;
    }
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);

	}
    
    @Override
	public void onStart() {
		super.onStart();
		/** Setting the multiselect choice mode for the listview */
		getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	}
    @Override
	public void onActivityCreated(Bundle savedState) {
    	super.onActivityCreated(savedState);
    	savedComplaints = DBAdapter.getAllComplaint();
    	adapter = new SavedComplaintsAdapter<Complaint>( mView, R.layout.saved_complaints_list_item_layout, savedComplaints);
		setListAdapter(adapter);
        getListView().setOnItemClickListener(
				new ListView.OnItemClickListener() {
					// @Override
					public void onItemClick(AdapterView<?> a, View v, int i,
							long l) {
						// do click action
						Complaint comp = savedComplaints.get(i);
						comp.setFiles(DBAdapter.getComplaintAttachment(comp.getId()));
//						mView.displaySavedComplaintDetails(comp);
						mView.displayEditComplaintView(comp);
					}
				});

		getListView().setOnItemLongClickListener(new OnItemLongClickListener() {

			// @Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// edit event here

				return true;
			}
		});
    }
}

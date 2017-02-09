package com.ebttikarat.complaints.gui.view;

import java.util.ArrayList;
import java.util.List;

import com.ebttikarat.complaints.client.OrderCoordinator;
import com.ebttikarat.complaints.client.RequestFactory;
import com.ebttikarat.complaints.client.common.Response;
import com.ebttikarat.complaints.client.processing.ResponseProcessingHelper;
import com.ebttikarat.complaints.common.model.Complaint;
import com.ebttikarat.complaints.gui.CustomProgressDialog;
import com.ebttikarat.complaints.gui.IActivity;
import com.ebttikarat.complaints.gui.R;
import com.ebttikarat.complaints.gui.adapter.MyComplaintsAdapter;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemLongClickListener;

public class MyComplaintsView extends ListFragment implements IActivity {

	private MyComplaintsAdapter<Complaint> adapter;
	private MainView mView;
	private CustomProgressDialog progress;
	private List<Complaint> myComplaints = new ArrayList<Complaint>();

	public MyComplaintsView(MainView context) {
		this.mView = context;
		progress = new CustomProgressDialog(context);
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
		adapter = new MyComplaintsAdapter<Complaint>(mView,
				R.layout.my_complaints_list_item_layout, myComplaints);
		setListAdapter(adapter);
		try {
			OrderCoordinator.handleOrder(this,
					RequestFactory.getComplaintsRequest());
		} catch (Exception ex) {

		}
		getListView().setOnItemClickListener(
				new ListView.OnItemClickListener() {
					// @Override
					public void onItemClick(AdapterView<?> a, View v, int i,
							long l) {
						// do click action
						Complaint comp = myComplaints.get(i);
						mView.displayMyComplaintDetails(comp);

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

	public void showProgressBar() {
		if (progress != null) {
			progress.show();
		}
	}

	public void closeProgress() {
		if (progress != null) {
			progress.dismiss();
		}
	}

	@Override
	public void preExecution() {
		// TODO Auto-generated method stub
		showProgressBar();
	}

	@Override
	public void postExecution(Response response) {
		// TODO Auto-generated method stub
		closeProgress();
		try {
			myComplaints = ResponseProcessingHelper.getInstance()
					.handleGetComplaintsResponse(response);
			adapter = new MyComplaintsAdapter<Complaint>(mView,
					R.layout.my_complaints_list_item_layout, myComplaints);
			setListAdapter(adapter);
		} catch (Exception ex) {

		}
	}
}

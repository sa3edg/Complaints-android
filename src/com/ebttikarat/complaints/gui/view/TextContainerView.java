package com.ebttikarat.complaints.gui.view;

import com.ebttikarat.complaints.client.OrderCoordinator;
import com.ebttikarat.complaints.client.RequestFactory;
import com.ebttikarat.complaints.client.common.Response;
import com.ebttikarat.complaints.client.processing.ResponseProcessingHelper;
import com.ebttikarat.complaints.gui.AbstractFragment;
import com.ebttikarat.complaints.gui.CustomProgressDialog;
import com.ebttikarat.complaints.gui.R;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class TextContainerView extends AbstractFragment {

	private TextView textView;
	private CustomProgressDialog progress;
	private String pageId;
	public TextContainerView(MainView mView, String pageId) {
		super(mView);
		progress = new CustomProgressDialog(mView);
		this.pageId = pageId;
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.text_container_layout,
				container, false);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedState) {
		super.onActivityCreated(savedState);
		textView = (TextView) activity
				.findViewById(R.id.textCnt);
		try {
			OrderCoordinator
					.handleOrder(this, RequestFactory.getPageContentRequest(pageId));
		} catch (Exception ex) {

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
		try{
			String content = ResponseProcessingHelper.getInstance().handleGetContentResponse(response);
			textView.setText(Html.fromHtml(content));
		}
		catch(Exception ex){
			
		}
		
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
}

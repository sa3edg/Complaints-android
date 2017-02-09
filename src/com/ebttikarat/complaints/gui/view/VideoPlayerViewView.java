package com.ebttikarat.complaints.gui.view;

import java.io.File;

import com.ebttikarat.complaints.client.common.Response;
import com.ebttikarat.complaints.gui.AbstractFragment;
import com.ebttikarat.complaints.gui.R;

import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoPlayerViewView extends AbstractFragment {

//	private AddNewComplaintView parentFg;
	private VideoView video;
	private MediaController ctlr;
	private String filePath;

	public VideoPlayerViewView(MainView mView, AddNewComplaintView parentFg,
			String path) {
		super(mView);
//		this.parentFg = parentFg;
		this.filePath = path;
		// TODO Auto-generated constructor stub
	}

	public VideoPlayerViewView(MainView mView) {
		super(mView);
		// TODO Auto-generated constructor stub
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.video_player_layout,
				container, false);
		return rootView;
	}

	@Override
	public void onActivityCreated(Bundle savedState) {
		super.onActivityCreated(savedState);
		getActivity().getWindow().setFormat(PixelFormat.TRANSLUCENT);
		File clip = new File(filePath);
		//start playing video
		if (clip.exists()) {
			video = (VideoView) activity.findViewById(R.id.video);
			video.setVideoPath(clip.getAbsolutePath());

			ctlr = new MediaController(activity);
			ctlr.setMediaPlayer(video);
			video.setMediaController(ctlr);
			video.requestFocus();
			video.start();
		}
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

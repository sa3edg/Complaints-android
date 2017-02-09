package com.ebttikarat.complaints.gui.adapter;

import java.io.File;

import com.ebttikarat.complaints.gui.R;
import com.ebttikarat.complaints.gui.model.FileBrowserItem;
import com.ebttikarat.complaints.gui.view.MainView;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.VideoView;

/**
 * 
 * @author Said Gamal
 * 
 */
public class AttachmentListAdapter extends ArrayAdapter<FileBrowserItem> {
	// private Context context;
	private int layoutResourceId;
	// private List<FileBrowserItem> data = new ArrayList<FileBrowserItem>();
	private MediaController ctlr;
	private MainView mView;
	private boolean isEditMode;

	public AttachmentListAdapter(MainView context, int layoutResourceId) {
		super(context, layoutResourceId, context.getCurrentAddComplaint()
				.getAttachments());
		this.layoutResourceId = layoutResourceId;
		this.mView = context;
	}
	public AttachmentListAdapter(MainView context, int layoutResourceId,
			boolean isEditMode) {
		super(context, layoutResourceId, context.getCurrentEditComplaint()
				.getAttachments());
		this.layoutResourceId = layoutResourceId;
		this.mView = context;
		this.isEditMode = isEditMode;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		RecordHolder holder = null;

		if (row == null) {
			LayoutInflater inflater = ((Activity) mView).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);

			holder = new RecordHolder();
			holder.play = (ImageButton) row.findViewById(R.id.playBtn);
			holder.delete = (ImageButton) row.findViewById(R.id.deleteBtn);
			holder.imageItem = (ImageView) row.findViewById(R.id.thumbImage);
			row.setTag(holder);
		} else {
			holder = (RecordHolder) row.getTag();
		}
		if (isEditMode) {
			if (mView.getCurrentEditComplaint() != null && mView.getCurrentEditComplaint().getAttachments().size() > 0) {
				final FileBrowserItem item = mView.getCurrentEditComplaint()
						.getAttachments().get(position);
				if (item.isVideo()) {
					holder.play.setVisibility(View.VISIBLE);
					Bitmap bm = ThumbnailUtils.createVideoThumbnail(
							item.getPath(),
							MediaStore.Images.Thumbnails.MINI_KIND);
					holder.imageItem.setImageBitmap(bm);
				} else {
					Drawable img = Drawable.createFromPath(item.getPath());
					holder.imageItem.setImageDrawable(img);
					holder.imageItem.setOnClickListener(new OnClickListener() {

						public void onClick(View arg0) {
							showImageViewer(item.getPath());
						}

					});
				}
				final int index = position;
				holder.play.setOnClickListener(new OnClickListener() {

					public void onClick(View arg0) {
						showMediaPlayer(item.getPath());
					}

				});
				holder.delete.setOnClickListener(new OnClickListener() {

					public void onClick(View arg0) {
						mView.getCurrentEditComplaint().getAttachments()
								.remove(index);
						notifyDataSetChanged();
					}

				});
			}
		} else {
			if (mView.getCurrentAddComplaint() != null && mView.getCurrentAddComplaint().getAttachments().size() > 0) {
				final FileBrowserItem item = mView.getCurrentAddComplaint()
						.getAttachments().get(position);
				if (item.isVideo()) {
					holder.play.setVisibility(View.VISIBLE);
					Bitmap bm = ThumbnailUtils.createVideoThumbnail(
							item.getPath(),
							MediaStore.Images.Thumbnails.MINI_KIND);
					holder.imageItem.setImageBitmap(bm);
				} else {
					Drawable img = Drawable.createFromPath(item.getPath());
					holder.imageItem.setImageDrawable(img);
					holder.imageItem.setOnClickListener(new OnClickListener() {

						public void onClick(View arg0) {
							showImageViewer(item.getPath());
						}

					});
				}
				final int index = position;
				holder.play.setOnClickListener(new OnClickListener() {

					public void onClick(View arg0) {
						showMediaPlayer(item.getPath());
					}

				});
				holder.delete.setOnClickListener(new OnClickListener() {

					public void onClick(View arg0) {
						mView.getCurrentAddComplaint().getAttachments()
								.remove(index);
						notifyDataSetChanged();
					}

				});
			}
		}
		return row;

	}

	private void showImageViewer(String filePath) {
		final Dialog mplayerDialog = new Dialog(mView);
		mplayerDialog.setCancelable(true);
		mplayerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mplayerDialog.setContentView(R.layout.image_viewer_layout);
		
		// get the Refferences of views
		mView.getWindow().setFormat(PixelFormat.TRANSLUCENT);
		Drawable img = Drawable.createFromPath(filePath);
		ImageView imageV = (ImageView) mplayerDialog
				.findViewById(R.id.img);
		imageV.setImageDrawable(img);
		final ImageButton cancel = (ImageButton) mplayerDialog
				.findViewById(R.id.deleteBtn);
		// Set On ClickListener
		cancel.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				mplayerDialog.dismiss();
			}
		});
		mplayerDialog.show();
	}
	private void showMediaPlayer(String filePath) {
		final Dialog mplayerDialog = new Dialog(mView);
		mplayerDialog.setCancelable(true);
		mplayerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		// dialog.getWindow().getDecorView()
		// .setBackgroundResource(android.R.color.transparent);
		mplayerDialog.setContentView(R.layout.video_player_layout);
		// changePasswordDialog.setTitle(activity.getResources().getText(
		// R.string.changeMyPassword));

		// get the Refferences of views
		mView.getWindow().setFormat(PixelFormat.TRANSLUCENT);
		File clip = new File(filePath);
		// start playing video
		if (clip.exists()) {
			VideoView video = (VideoView) mplayerDialog
					.findViewById(R.id.video);
			video.setVideoPath(clip.getAbsolutePath());

			ctlr = new MediaController(mView);
			ctlr.setMediaPlayer(video);
			video.setMediaController(ctlr);
			video.requestFocus();
			video.start();
//			video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() 
//	        {           
//	            public void onCompletion(MediaPlayer mp) 
//	            {
//	                // Do whatever u need to do here
//	            	mplayerDialog.dismiss();
//	            }           
//	        });   
		}
		

		final ImageButton cancel = (ImageButton) mplayerDialog
				.findViewById(R.id.deleteBtn);
		// Set On ClickListener
		cancel.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				mplayerDialog.dismiss();
			}
		});
		mplayerDialog.show();
	}

	static class RecordHolder {
		ImageButton play;
		ImageButton delete;
		ImageView imageItem;

	}
}
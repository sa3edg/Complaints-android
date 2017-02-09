package com.ebttikarat.complaints.gui.adapter;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import com.ebttikarat.complaints.common.model.Attachment;
import com.ebttikarat.complaints.gui.R;
import com.ebttikarat.complaints.gui.view.MainView;
import com.ebttikarat.complaints.handler.ImagesMemoryHandler;
import com.ebttikarat.complaints.util.FileUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaRecorder;
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
public class AttachmentDetailsListAdapter extends ArrayAdapter<Attachment> {
	// private Context context;
	private int layoutResourceId;
	// private List<FileBrowserItem> data = new ArrayList<FileBrowserItem>();
	private MediaController ctlr;
	private MainView mView;
	private List<Attachment> attachment;
	private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

	public AttachmentDetailsListAdapter(MainView context, int layoutResourceId,
			List<Attachment> attachments) {
		super(context, layoutResourceId, attachments);
		this.layoutResourceId = layoutResourceId;
		this.mView = context;
		this.attachment = attachments;
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
		if (attachment.size() > 0) {
			final Attachment item = attachment.get(position);
			if (FileUtils.isVideo(item.getFilePath())) {
				holder.play.setVisibility(View.VISIBLE);
				holder.imageItem.setImageDrawable(mView.getResources()
						.getDrawable(R.drawable.video_bg));
			} else {
				ImageLoader.getInstance().displayImage(item.getFilePath(),
						holder.imageItem,
						ImagesMemoryHandler.getDisplayImageOptions(),
						animateFirstListener);
				holder.imageItem.setOnClickListener(new OnClickListener() {

					public void onClick(View arg0) {
						showImageViewer(item.getFilePath());
					}

				});
			}
			holder.play.setOnClickListener(new OnClickListener() {

				public void onClick(View arg0) {
					showMediaPlayer(item.getFilePath());
				}

			});
			holder.delete.setVisibility(View.GONE);

		}
		return row;

	}

	private void showImageViewer(String path) {
		final Dialog mplayerDialog = new Dialog(mView);
		mplayerDialog.setCancelable(true);
		mplayerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		mplayerDialog.setContentView(R.layout.image_viewer_layout);

		// get the Refferences of views
		mView.getWindow().setFormat(PixelFormat.TRANSLUCENT);
//		Drawable img = Drawable.createFromPath(filePath);
		ImageView imageV = (ImageView) mplayerDialog.findViewById(R.id.img);
		ImageLoader.getInstance().displayImage(path,
				imageV,
				ImagesMemoryHandler.getDisplayImageOptions(),
				animateFirstListener);
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

	private void showMediaPlayer(String url) {
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
		// start playing video
		VideoView video = (VideoView) mplayerDialog.findViewById(R.id.video);
		video.setVideoPath(url);

		ctlr = new MediaController(mView);
		ctlr.setMediaPlayer(video);
		video.setMediaController(ctlr);
		video.requestFocus();
		video.start();
//		video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//			public void onCompletion(MediaPlayer mp) {
//				// Do whatever u need to do here
//				mplayerDialog.dismiss();
//			}
//		});
//		video.setOnInfoListener(new OnInfoListener() {
//			@Override
//			public boolean onInfo(MediaPlayer arg0, int arg1, int arg2) {
//				// TODO Auto-generated method stub
//				return false;
//			}
//		});

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

	private static class AnimateFirstDisplayListener extends
			SimpleImageLoadingListener {

		static final List<String> displayedImages = Collections
				.synchronizedList(new LinkedList<String>());

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			if (loadedImage != null) {
				ImageView imageView = (ImageView) view;
				boolean firstDisplay = !displayedImages.contains(imageUri);
				if (firstDisplay) {
					FadeInBitmapDisplayer.animate(imageView, 500);
					displayedImages.add(imageUri);
				} else {
					imageView.setImageBitmap(loadedImage);
				}
			}
		}
	}

	static class RecordHolder {
		ImageButton play;
		ImageButton delete;
		ImageView imageItem;

	}
}
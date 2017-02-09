package com.ebttikarat.complaints.gui.adapter;

import java.util.List;

import com.ebttikarat.complaints.gui.R;
import com.ebttikarat.complaints.gui.model.FileBrowserItem;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class FileArrayAdapter extends ArrayAdapter<FileBrowserItem> {

	private Context context;
	private int id;
	private List<FileBrowserItem> items;

	public FileArrayAdapter(Context context, int textViewResourceId,
			List<FileBrowserItem> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
		id = textViewResourceId;
		items = objects;
	}

	public FileBrowserItem getItem(int i) {
		return items.get(i);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		ViewHolder holder = null;
		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(id, parent, false);
			holder = new ViewHolder();
			holder.t1 = (TextView) row.findViewById(R.id.TextView01);
			holder.t2 = (TextView) row.findViewById(R.id.TextView02);
			holder.t3 = (TextView) row.findViewById(R.id.TextViewDate);
			holder.imageCity = (ImageView) row.findViewById(R.id.fd_Icon1);
			row.setTag(holder);
		} else {
			holder = (ViewHolder) row.getTag();
		}

		final FileBrowserItem o = items.get(position);
		if (o != null) {
			String uri = "drawable/" + o.getImage();
			int imageResource = context.getResources().getIdentifier(uri, null,
					context.getPackageName());
			Drawable image = context.getResources().getDrawable(imageResource);
			holder.imageCity.setImageDrawable(image);

			if (holder.t1 != null)
				holder.t1.setText(o.getName());
			if (holder.t2 != null)
				holder.t2.setText(o.getData());
			if (holder.t3 != null)
				holder.t3.setText(o.getDate());

		}
		return row;
	}

	static class ViewHolder {
		TextView t1;
		TextView t2;
		TextView t3;
		ImageView imageCity;
	}

}
